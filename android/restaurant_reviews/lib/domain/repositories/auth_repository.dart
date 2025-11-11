import '../entities/user_profile.dart';

abstract class AuthRepository {
  // Stream trạng thái đăng nhập
  Stream<UserProfile?> authState();

  // Đăng nhập Email/Password
  Future<UserProfile?> signInWithEmail(String email, String password);

  // Đăng ký Email/Password
  Future<UserProfile?> registerWithEmail(
    String email,
    String password, {
    String? displayName,
  });

  // Quên mật khẩu (gửi email reset)
  Future<void> sendPasswordReset(String email);

  // (Giữ lại nếu bạn vẫn muốn dùng Google sau này)
  Future<UserProfile?> signInWithGoogle();

  Future<void> signOut();
}
