import 'package:flutter/foundation.dart';
import 'package:firebase_auth/firebase_auth.dart';

import '../models/app_user.dart';
import '../services/auth_service.dart';

class AuthProvider extends ChangeNotifier {
  final AuthService _authService;

  AuthProvider(this._authService) {
    // Lắng nghe trạng thái đăng nhập của Firebase
    _authService.authStateChanges.listen(_onAuthStateChanged);
  }

  AppUser? currentUser;

  /// Đang khởi tạo app lần đầu (RootScreen sẽ dùng cờ này)
  bool isInitializing = true;

  /// Đang xử lý form login/register (LoginScreen dùng cờ này)
  bool isLoading = false;

  // ====== STREAM CALLBACK ======
  Future<void> _onAuthStateChanged(User? firebaseUser) async {
    if (firebaseUser == null) {
      currentUser = null;
    } else {
      // Lấy dữ liệu user từ collection "users"
      currentUser = await _authService.getCurrentAppUser();
    }

    // Khởi tạo xong
    isInitializing = false;
    notifyListeners();
  }

  // ====== LOGIN ======
  Future<void> login(String email, String password) async {
    isLoading = true;
    notifyListeners();

    try {
      currentUser = await _authService.login(email, password);
      // Sau khi login, authStateChanges cũng sẽ bắn event, nhưng có currentUser sẵn cũng không sao
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  // ====== REGISTER (CÓ ROLE) ======
  Future<void> register({
    required String email,
    required String password,
    required String displayName,
    String role = 'user',
  }) async {
    isLoading = true;
    notifyListeners();

    try {
      currentUser = await _authService.register(
        email: email,
        password: password,
        displayName: displayName,
        role: role,
      );
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  // ====== LOGOUT ======
  Future<void> logout() async {
    await _authService.logout();
    currentUser = null;
    notifyListeners();
  }

  bool get isLibrarian => currentUser?.role == 'librarian';
}
