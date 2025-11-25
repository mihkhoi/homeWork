import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

import '../providers/auth_provider.dart';
import '../services/cloudinary_service.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  bool _isUploading = false;
  final _cloudinaryService = CloudinaryService();
  final _picker = ImagePicker();

  Future<void> _changeAvatar() async {
    final auth = context.read<AuthProvider>();
    final user = auth.user;
    if (user == null) return;

    final pickedFile = await _picker.pickImage(
      source: ImageSource.gallery,
      imageQuality: 75,
    );

    if (pickedFile == null) return;

    setState(() => _isUploading = true);
    try {
      final url = await _cloudinaryService.uploadImage(pickedFile);
      if (url != null) {
        await auth.updateAvatar(url);
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Lỗi upload ảnh: $e')));
    } finally {
      if (mounted) setState(() => _isUploading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final profile = auth.userProfile ?? {};
    final avatarUrl = profile['avatarUrl'] as String?;
    final email = profile['email'] as String? ?? auth.user?.email ?? '';

    return Scaffold(
      appBar: AppBar(title: const Text('Thông tin cá nhân')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CircleAvatar(
              radius: 60,
              backgroundImage: avatarUrl != null
                  ? NetworkImage(avatarUrl)
                  : null,
              child: avatarUrl == null
                  ? const Icon(Icons.person, size: 60)
                  : null,
            ),
            const SizedBox(height: 12),
            Text(email),
            const SizedBox(height: 16),
            _isUploading
                ? const CircularProgressIndicator()
                : ElevatedButton(
                    onPressed: _changeAvatar,
                    child: const Text('Đổi ảnh đại diện'),
                  ),
          ],
        ),
      ),
    );
  }
}
