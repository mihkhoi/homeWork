import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../providers/auth_provider.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _classIdController =
      TextEditingController(); // thêm controller cho classId

  bool _isLogin = true; // true = đăng nhập, false = đăng ký
  bool _isLoading = false;

  String _selectedRole = 'student'; // mặc định: Học sinh

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    final auth = context.read<AuthProvider>();
    setState(() => _isLoading = true);

    try {
      if (_isLogin) {
        // ĐĂNG NHẬP
        await auth.login(
          _emailController.text.trim(),
          _passwordController.text.trim(),
        );
      } else {
        // ĐĂNG KÝ
        final classId = (_selectedRole == 'student')
            ? _classIdController.text.trim()
            : null;

        await auth.register(
          _emailController.text.trim(),
          _passwordController.text.trim(),
          _selectedRole,
          classId:
              classId, // gửi kèm classId (có thể null nếu không phải student)
        );

        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Đăng ký thành công, hãy đăng nhập!')),
        );

        setState(() {
          _isLogin = true; // quay lại chế độ đăng nhập
        });
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Lỗi: $e')));
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    _classIdController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final isRegister = !_isLogin;

    return Scaffold(
      appBar: AppBar(title: Text(_isLogin ? 'Đăng nhập' : 'Đăng ký')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              // ===== Email =====
              TextFormField(
                controller: _emailController,
                decoration: const InputDecoration(labelText: 'Email'),
                keyboardType: TextInputType.emailAddress,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Vui lòng nhập email';
                  }
                  if (!value.contains('@')) {
                    return 'Email không hợp lệ';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 8),

              // ===== Mật khẩu =====
              TextFormField(
                controller: _passwordController,
                decoration: const InputDecoration(labelText: 'Mật khẩu'),
                obscureText: true,
                validator: (value) {
                  if (value == null || value.length < 6) {
                    return 'Mật khẩu phải >= 6 ký tự';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 8),

              // ===== Chọn vai trò khi ĐĂNG KÝ =====
              if (isRegister) ...[
                DropdownButtonFormField<String>(
                  initialValue: _selectedRole,
                  decoration: const InputDecoration(labelText: 'Vai trò'),
                  items: const [
                    DropdownMenuItem(value: 'student', child: Text('Học sinh')),
                    DropdownMenuItem(value: 'parent', child: Text('Phụ huynh')),
                    DropdownMenuItem(
                      value: 'teacher',
                      child: Text('Giáo viên'),
                    ),
                  ],
                  onChanged: (value) {
                    if (value != null) {
                      setState(() => _selectedRole = value);
                    }
                  },
                ),
                const SizedBox(height: 8),

                // Nếu là HỌC SINH thì hiện thêm ô Lớp (classId)
                if (_selectedRole == 'student') ...[
                  TextFormField(
                    controller: _classIdController,
                    decoration: const InputDecoration(
                      labelText: 'Lớp (VD: 10A1)',
                    ),
                    validator: (v) {
                      // chỉ validate khi role là student
                      if (_selectedRole == 'student') {
                        if (v == null || v.isEmpty) {
                          return 'Vui lòng nhập lớp (classId)';
                        }
                      }
                      return null;
                    },
                  ),
                ],
              ],

              const SizedBox(height: 16),

              // ===== Nút hành động =====
              if (_isLoading)
                const Center(child: CircularProgressIndicator())
              else ...[
                ElevatedButton(
                  onPressed: _submit,
                  child: Text(_isLogin ? 'Đăng nhập' : 'Đăng ký'),
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
            ],
          ),
        ),
      ),
    );
  }
}
