import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/auth_provider.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _emailCtrl = TextEditingController();
  final _passwordCtrl = TextEditingController();

  bool _isLogin = true; // true: đăng nhập, false: đăng ký
  String _selectedRole = 'user'; // 'user' hoặc 'librarian'

  @override
  void dispose() {
    _emailCtrl.dispose();
    _passwordCtrl.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    final auth = context.read<AuthProvider>();

    try {
      if (_isLogin) {
        // ĐĂNG NHẬP
        await auth.login(_emailCtrl.text.trim(), _passwordCtrl.text.trim());
      } else {
        // ĐĂNG KÝ THEO ROLE
        await auth.register(
          email: _emailCtrl.text.trim(),
          password: _passwordCtrl.text.trim(),
          displayName: _emailCtrl.text.trim().split('@').first,
          role: _selectedRole,
        );
      }
      // RootScreen sẽ nghe currentUser và tự chuyển sang HomeScreen
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Lỗi: $e')));
    }
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final isRegister = !_isLogin;

    return Scaffold(
      appBar: AppBar(title: Text(_isLogin ? 'Đăng nhập' : 'Đăng ký')),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                const SizedBox(height: 16),
                TextFormField(
                  controller: _emailCtrl,
                  decoration: const InputDecoration(labelText: 'Email'),
                  keyboardType: TextInputType.emailAddress,
                  validator: (v) =>
                      v == null || v.isEmpty ? 'Nhập email' : null,
                ),
                const SizedBox(height: 12),
                TextFormField(
                  controller: _passwordCtrl,
                  decoration: const InputDecoration(labelText: 'Mật khẩu'),
                  obscureText: true,
                  validator: (v) =>
                      v == null || v.length < 6 ? 'Mật khẩu >= 6 ký tự' : null,
                ),

                // ================== CHỌN ROLE KHI ĐĂNG KÝ ==================
                if (isRegister) const SizedBox(height: 16),
                if (isRegister)
                  DropdownButtonFormField<String>(
                    // dùng initialValue thay cho value để tránh deprecated
                    initialValue: _selectedRole,
                    decoration: const InputDecoration(
                      labelText: 'Vai trò (Role)',
                    ),
                    items: const [
                      DropdownMenuItem(
                        value: 'user',
                        child: Text('Người dùng'),
                      ),
                      DropdownMenuItem(
                        value: 'librarian',
                        child: Text('Thủ thư / Quản trị'),
                      ),
                    ],
                    onChanged: (value) {
                      if (value == null) return;
                      setState(() {
                        _selectedRole = value;
                      });
                    },
                  ),

                // ===========================================================
                const SizedBox(height: 24),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: auth.isLoading ? null : _submit,
                    child: Text(
                      auth.isLoading
                          ? 'Đang xử lý...'
                          : (_isLogin ? 'Đăng nhập' : 'Đăng ký'),
                    ),
                  ),
                ),
                TextButton(
                  onPressed: () {
                    setState(() {
                      _isLogin = !_isLogin;
                    });
                  },
                  child: Text(
                    _isLogin
                        ? 'Chưa có tài khoản? Đăng ký'
                        : 'Đã có tài khoản? Đăng nhập',
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
