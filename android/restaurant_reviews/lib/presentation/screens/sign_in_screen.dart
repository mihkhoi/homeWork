import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../domain/repositories/auth_repository.dart';

class SignInScreen extends StatefulWidget {
  const SignInScreen({super.key});

  @override
  State<SignInScreen> createState() => _SignInScreenState();
}

class _SignInScreenState extends State<SignInScreen> {
  final _form = GlobalKey<FormState>();
  final _emailCtl = TextEditingController();
  final _passCtl = TextEditingController();
  final _nameCtl = TextEditingController(); // chỉ dùng khi đăng ký
  bool _isLogin = true;
  bool _obscure = true;
  bool _loading = false;

  @override
  void dispose() {
    _emailCtl.dispose();
    _passCtl.dispose();
    _nameCtl.dispose();
    super.dispose();
  }

  String? _emailValidator(String? v) {
    if (v == null || v.trim().isEmpty) return 'Nhập email';
    final ok = RegExp(r'^[^@]+@[^@]+\.[^@]+$').hasMatch(v.trim());
    if (!ok) return 'Email không hợp lệ';
    return null;
  }

  String? _passValidator(String? v) {
    if (v == null || v.isEmpty) return 'Nhập mật khẩu';
    if (v.length < 6) return 'Tối thiểu 6 ký tự';
    return null;
  }

  Future<void> _submit() async {
    if (!_form.currentState!.validate()) return;
    final auth = context.read<AuthRepository>();
    setState(() => _loading = true);
    try {
      if (_isLogin) {
        await auth.signInWithEmail(_emailCtl.text.trim(), _passCtl.text);
      } else {
        await auth.registerWithEmail(
          _emailCtl.text.trim(),
          _passCtl.text,
          displayName: _nameCtl.text.trim().isEmpty
              ? null
              : _nameCtl.text.trim(),
        );
      }
      // Root sẽ tự điều hướng vì nghe authState()
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text(_friendlyError(e))));
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  String _friendlyError(Object e) {
    final msg = e.toString();
    if (msg.contains('invalid-credential') || msg.contains('user-not-found')) {
      return 'Sai email hoặc mật khẩu';
    }
    if (msg.contains('wrong-password')) return 'Mật khẩu không đúng';
    if (msg.contains('email-already-in-use')) return 'Email đã tồn tại';
    if (msg.contains('network-request-failed')) return 'Lỗi mạng, thử lại';
    return 'Đăng nhập thất bại: $msg';
  }

  Future<void> _resetPassword() async {
    if (_emailCtl.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Nhập email trước khi khôi phục mật khẩu'),
        ),
      );
      return;
    }
    try {
      await context.read<AuthRepository>().sendPasswordReset(
        _emailCtl.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Đã gửi email khôi phục mật khẩu')),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text(_friendlyError(e))));
    }
  }

  @override
  Widget build(BuildContext context) {
    final title = _isLogin ? 'Đăng nhập' : 'Tạo tài khoản';
    return Scaffold(
      appBar: AppBar(title: Text(title)),
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 420),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Form(
              key: _form,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (!_isLogin) ...[
                    TextFormField(
                      controller: _nameCtl,
                      decoration: const InputDecoration(
                        labelText: 'Tên hiển thị (tuỳ chọn)',
                        prefixIcon: Icon(Icons.person_outline),
                      ),
                    ),
                    const SizedBox(height: 12),
                  ],
                  TextFormField(
                    controller: _emailCtl,
                    validator: _emailValidator,
                    keyboardType: TextInputType.emailAddress,
                    decoration: const InputDecoration(
                      labelText: 'Email',
                      prefixIcon: Icon(Icons.mail_outline),
                    ),
                  ),
                  const SizedBox(height: 12),
                  TextFormField(
                    controller: _passCtl,
                    validator: _passValidator,
                    obscureText: _obscure,
                    decoration: InputDecoration(
                      labelText: 'Mật khẩu',
                      prefixIcon: const Icon(Icons.lock_outline),
                      suffixIcon: IconButton(
                        onPressed: () => setState(() => _obscure = !_obscure),
                        icon: Icon(
                          _obscure ? Icons.visibility : Icons.visibility_off,
                        ),
                      ),
                    ),
                  ),
                  Align(
                    alignment: Alignment.centerRight,
                    child: TextButton(
                      onPressed: _isLogin ? _resetPassword : null,
                      child: const Text('Quên mật khẩu?'),
                    ),
                  ),
                  const SizedBox(height: 8),
                  SizedBox(
                    width: double.infinity,
                    child: FilledButton.icon(
                      onPressed: _loading ? null : _submit,
                      icon: _loading
                          ? const SizedBox(
                              width: 18,
                              height: 18,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Icon(Icons.login),
                      label: Text(_isLogin ? 'Đăng nhập' : 'Đăng ký'),
                    ),
                  ),
                  const SizedBox(height: 8),
                  TextButton(
                    onPressed: () => setState(() => _isLogin = !_isLogin),
                    child: Text(
                      _isLogin
                          ? 'Chưa có tài khoản? Tạo mới'
                          : 'Đã có tài khoản? Đăng nhập',
                    ),
                  ),

                  // (Tuỳ chọn) vẫn cho đăng nhập Google
                  // const Divider(height: 32),
                  // OutlinedButton.icon(
                  //   onPressed: _loading ? null : () async {
                  //     setState(() => _loading = true);
                  //     try { await context.read<AuthRepository>().signInWithGoogle(); }
                  //     catch (e) { if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(_friendlyError(e)))); }
                  //     finally { if (mounted) setState(() => _loading = false); }
                  //   },
                  //   icon: const Icon(Icons.g_mobiledata_rounded),
                  //   label: const Text('Đăng nhập Google'),
                  // ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
