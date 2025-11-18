import 'package:flutter/material.dart';
import '../models/app_user.dart';
import '../services/auth_service.dart';

class AuthProvider extends ChangeNotifier {
  final AuthService _authService;

  AppUser? _user;
  AppUser? get user => _user;

  AuthProvider(this._authService) {
    _authService.authStateChanges().listen((u) {
      _user = u;
      notifyListeners();
    });
  }

  bool get isLoggedIn => _user != null;

  Future<void> login(String email, String password) =>
      _authService.login(email: email, password: password);

  Future<void> register(String email, String password, String displayName) =>
      _authService.register(
        email: email,
        password: password,
        displayName: displayName,
      );

  Future<void> logout() => _authService.logout();
}
