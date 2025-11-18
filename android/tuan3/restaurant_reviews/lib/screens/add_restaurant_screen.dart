import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

import '../services/restaurant_service.dart';

class AddRestaurantScreen extends StatefulWidget {
  final RestaurantService service;

  const AddRestaurantScreen({super.key, required this.service});

  @override
  State<AddRestaurantScreen> createState() => _AddRestaurantScreenState();
}

class _AddRestaurantScreenState extends State<AddRestaurantScreen> {
  final _nameCtrl = TextEditingController();
  final _addressCtrl = TextEditingController();
  final _imageUrlCtrl = TextEditingController();

  bool _loading = false;
  String? _error;

  final ImagePicker _picker = ImagePicker();
  XFile? _pickedImage;

  Future<void> _pickImage() async {
    final img = await _picker.pickImage(source: ImageSource.gallery);
    if (img != null) {
      setState(() => _pickedImage = img);
    }
  }

  Future<void> _submit() async {
    if (_nameCtrl.text.trim().isEmpty || _addressCtrl.text.trim().isEmpty) {
      setState(() {
        _error = 'Vui lòng nhập tên và địa chỉ';
      });
      return;
    }

    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      await widget.service.addRestaurant(
        name: _nameCtrl.text.trim(),
        address: _addressCtrl.text.trim(),
        imageUrl: _imageUrlCtrl.text.trim(), // có thể rỗng
        imageFile: _pickedImage, // nếu chọn ảnh sẽ ưu tiên upload file
      );

      if (!mounted) return;
      Navigator.of(context).pop();
    } catch (e) {
      setState(() => _error = e.toString());
    } finally {
      if (mounted) {
        setState(() => _loading = false);
      }
    }
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _addressCtrl.dispose();
    _imageUrlCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thêm nhà hàng')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextField(
                controller: _nameCtrl,
                decoration: const InputDecoration(labelText: 'Tên nhà hàng'),
              ),
              TextField(
                controller: _addressCtrl,
                decoration: const InputDecoration(labelText: 'Địa chỉ'),
              ),
              const SizedBox(height: 16),

              // nút chọn ảnh từ máy
              Row(
                children: [
                  ElevatedButton.icon(
                    onPressed: _pickImage,
                    icon: const Icon(Icons.image),
                    label: const Text('Chọn ảnh từ máy'),
                  ),
                  const SizedBox(width: 8),
                  if (_pickedImage != null)
                    const Text(
                      'Đã chọn ảnh',
                      style: TextStyle(color: Colors.green),
                    ),
                ],
              ),
              const SizedBox(height: 8),

              // nhập URL ảnh (tùy chọn)
              TextField(
                controller: _imageUrlCtrl,
                decoration: const InputDecoration(
                  labelText: 'Hoặc nhập URL ảnh (có thể bỏ trống)',
                ),
              ),
              const SizedBox(height: 16),

              if (_error != null)
                Text(_error!, style: const TextStyle(color: Colors.red)),
              const SizedBox(height: 8),

              _loading
                  ? const Center(child: CircularProgressIndicator())
                  : SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _submit,
                        child: const Text('Lưu nhà hàng'),
                      ),
                    ),
            ],
          ),
        ),
      ),
    );
  }
}
