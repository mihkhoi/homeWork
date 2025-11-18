import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

import '../models/restaurant.dart';
import '../providers/auth_provider.dart';
import '../services/restaurant_service.dart';

class AddReviewScreen extends StatefulWidget {
  final Restaurant restaurant;
  final RestaurantService service;

  const AddReviewScreen({
    super.key,
    required this.restaurant,
    required this.service,
  });

  @override
  State<AddReviewScreen> createState() => _AddReviewScreenState();
}

class _AddReviewScreenState extends State<AddReviewScreen> {
  final _commentCtrl = TextEditingController();
  int _rating = 5;
  XFile? _pickedImage;
  bool _loading = false;
  String? _error;

  final ImagePicker _picker = ImagePicker();

  Future<void> _pickImage() async {
    final img = await _picker.pickImage(source: ImageSource.gallery);
    if (img != null) {
      setState(() => _pickedImage = img);
    }
  }

  Future<void> _submit() async {
    final auth = context.read<AuthProvider>();
    if (auth.user == null) {
      setState(() => _error = 'Bạn cần đăng nhập');
      return;
    }

    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      await widget.service.addReview(
        restaurantId: widget.restaurant.id,
        userId: auth.user!.uid,
        userName: auth.user!.displayName,
        rating: _rating,
        comment: _commentCtrl.text.trim(),
        imageFile: _pickedImage,
      );

      if (!mounted) return;
      Navigator.of(context).pop();
    } catch (e) {
      setState(() => _error = e.toString());
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Đánh giá ${widget.restaurant.name}')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text('Số sao'),
              DropdownButton<int>(
                value: _rating,
                items: List.generate(
                  5,
                  (i) => DropdownMenuItem(
                    value: i + 1,
                    child: Text('${i + 1} sao'),
                  ),
                ),
                onChanged: (v) {
                  if (v != null) {
                    setState(() => _rating = v);
                  }
                },
              ),
              const SizedBox(height: 8),
              const Text('Nhận xét'),
              TextField(
                controller: _commentCtrl,
                maxLines: 4,
                decoration: const InputDecoration(border: OutlineInputBorder()),
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  ElevatedButton.icon(
                    onPressed: _pickImage,
                    icon: const Icon(Icons.image),
                    label: const Text('Chọn ảnh'),
                  ),
                  const SizedBox(width: 8),
                  if (_pickedImage != null)
                    Text(
                      'Đã chọn ảnh',
                      style: TextStyle(
                        color: Theme.of(context).colorScheme.primary,
                      ),
                    ),
                ],
              ),
              const SizedBox(height: 8),
              if (_error != null)
                Text(_error!, style: const TextStyle(color: Colors.red)),
              const SizedBox(height: 8),
              _loading
                  ? const Center(child: CircularProgressIndicator())
                  : SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _submit,
                        child: const Text('Gửi đánh giá'),
                      ),
                    ),
            ],
          ),
        ),
      ),
    );
  }
}
