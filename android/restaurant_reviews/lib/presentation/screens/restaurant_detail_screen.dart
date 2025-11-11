import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:image/image.dart' as img; // <-- nén ảnh
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

import '../../domain/entities/restaurant.dart';
import '../providers/auth_provider.dart';
import '../providers/review_provider.dart';
import '../widgets/rating_bar.dart';
import '../widgets/review_tile.dart';
import '../../services/cloudinary_uploader.dart';

class RestaurantDetailScreen extends StatefulWidget {
  final Restaurant r;
  const RestaurantDetailScreen({super.key, required this.r});
  @override
  State<RestaurantDetailScreen> createState() => _RestaurantDetailScreenState();
}

class _RestaurantDetailScreenState extends State<RestaurantDetailScreen> {
  final _ctl = TextEditingController();
  int _rating = 5;
  Uint8List? _picked;
  String _pickedMime = 'image/jpeg';
  bool _submitting = false;

  // Điền thông tin Cloudinary của bạn (không có <>)
  final _uploader = const CloudinaryUploader(
    cloudName: 'dfol9bim3',
    uploadPreset: 'restaurant_reviews',
  );

  @override
  void initState() {
    super.initState();
    context.read<ReviewProvider>().watch(widget.r.id);
  }

  // Nén và chuẩn hoá ảnh (JPEG ~85 chất lượng, maxWidth 1600)
  Future<void> _pick() async {
    final x = await ImagePicker().pickImage(source: ImageSource.gallery);
    if (x == null) return;

    final bytes = await x.readAsBytes();
    // đoán mime từ phần mở rộng
    final ext = (x.name.split('.').last).toLowerCase();
    _pickedMime = (ext == 'png')
        ? 'image/png'
        : (ext == 'webp')
        ? 'image/webp'
        : 'image/jpeg';

    // nén về JPEG để nhẹ hơn, Cloudinary tiếp nhận tốt
    try {
      final decoded = img.decodeImage(bytes);
      if (decoded != null) {
        final resized = img.copyResize(
          decoded,
          width: decoded.width > 1600 ? 1600 : decoded.width,
        );
        final jpg = img.encodeJpg(resized, quality: 85);
        if (!mounted) return;
        setState(() {
          _picked = Uint8List.fromList(jpg);
          _pickedMime = 'image/jpeg';
        });
        return;
      }
    } catch (_) {
      // Nếu không nén được thì dùng bytes gốc
    }

    if (!mounted) return;
    setState(() {
      _picked = bytes;
    });
  }

  Future<void> _submit() async {
    final me = context.read<AuthProvider>().current;
    if (me == null) return;

    final reviewRepo = context.read<ReviewProvider>();
    setState(() => _submitting = true);

    try {
      String? url;
      if (_picked != null) {
        url = await _uploader.uploadBytes(
          _picked!,
          fileName:
              'r_${widget.r.id}_${DateTime.now().millisecondsSinceEpoch}.jpg',
          mime: _pickedMime,
        );
      }

      await reviewRepo.add(
        widget.r.id,
        userId: me.uid,
        userName: me.name ?? 'Ẩn danh',
        userAvatar: me.photoUrl,
        rating: _rating,
        content: _ctl.text.trim(),
        imageUrl: url,
      );

      if (!mounted) return;
      _ctl.clear();
      setState(() {
        _picked = null;
        _submitting = false;
      });
      FocusScope.of(context).unfocus();
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Đã đăng đánh giá')));
    } catch (e) {
      if (!mounted) return;
      setState(() => _submitting = false);
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Lỗi upload: $e')));
    }
  }

  @override
  Widget build(BuildContext context) {
    final reviews = context.watch<ReviewProvider>().items;
    return Scaffold(
      appBar: AppBar(title: Text(widget.r.name)),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: Row(
              children: [
                Expanded(child: Text(widget.r.address)),
                Text('⭐ ${widget.r.ratingAvg} (${widget.r.ratingCount})'),
              ],
            ),
          ),
          const Divider(height: 0),
          Expanded(
            child: ListView.builder(
              itemCount: reviews.length,
              itemBuilder: (_, i) => ReviewTile(r: reviews[i]),
            ),
          ),
          const Divider(height: 0),
          Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                RatingBar(
                  value: _rating,
                  onChanged: (v) => setState(() => _rating = v),
                  size: 24,
                ),
                const SizedBox(height: 8),
                TextField(
                  controller: _ctl,
                  decoration: const InputDecoration(
                    hintText: 'Chia sẻ trải nghiệm...',
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    OutlinedButton.icon(
                      onPressed: _submitting ? null : _pick,
                      icon: const Icon(Icons.image),
                      label: const Text('Ảnh'),
                    ),
                    const SizedBox(width: 8),
                    FilledButton.icon(
                      onPressed: _submitting ? null : _submit,
                      icon: _submitting
                          ? const SizedBox(
                              width: 18,
                              height: 18,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Icon(Icons.send),
                      label: Text(_submitting ? 'Đang gửi...' : 'Gửi'),
                    ),
                  ],
                ),
                if (_picked != null)
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: Image.memory(
                      _picked!,
                      height: 120,
                      fit: BoxFit.cover,
                    ),
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
