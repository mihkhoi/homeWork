import 'package:flutter/foundation.dart';
import '../../../domain/entities/user_profile.dart';
import '../../../domain/repositories/auth_repository.dart';

class AuthProvider extends ChangeNotifier {
  final AuthRepository repo;
  AuthProvider(this.repo) {
    _sub();
  }

  UserProfile? current;

  void _sub() {
    repo.authState().listen((u) {
      current = u;
      notifyListeners();
    });
  }

  Future<void> signOut() => repo.signOut();
}
