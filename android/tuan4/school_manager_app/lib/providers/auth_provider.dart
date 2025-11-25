import 'package:flutter/foundation.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

class AuthProvider extends ChangeNotifier {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  User? user;
  Map<String, dynamic>? _userProfile;

  /// Cờ để biết app đang khởi tạo lần đầu (để show màn loading)
  bool isInitializing = true;

  Map<String, dynamic>? get userProfile => _userProfile;

  AuthProvider() {
    _auth.authStateChanges().listen(_onAuthStateChanged);
  }

  Future<void> _onAuthStateChanged(User? firebaseUser) async {
    user = firebaseUser;

    try {
      if (user != null) {
        final doc = await _firestore.collection('users').doc(user!.uid).get();
        _userProfile = doc.data();
      } else {
        _userProfile = null;
      }
    } catch (e) {
      // Nếu lỗi (permission, network, ...) thì không cho app crash
      _userProfile = null;
      debugPrint('Error loading user profile: $e');
    }

    isInitializing = false;
    notifyListeners();
  }

  Future<void> login(String email, String password) async {
    await _auth.signInWithEmailAndPassword(email: email, password: password);
    // _onAuthStateChanged sẽ tự chạy
  }

  /// Đăng ký user mới
  /// [role]: 'student' | 'parent' | 'teacher'
  /// [classId]: chỉ dùng cho học sinh (VD: 10A1)
  Future<void> register(
    String email,
    String password,
    String role, {
    String? classId,
  }) async {
    final cred = await _auth.createUserWithEmailAndPassword(
      email: email,
      password: password,
    );

    final uid = cred.user!.uid;

    // Build dữ liệu lưu vào collection "users"
    final data = <String, dynamic>{'email': email, 'role': role};

    // Nếu là học sinh và có nhập classId thì lưu thêm
    if (role == 'student' && classId != null && classId.isNotEmpty) {
      data['classId'] = classId;
    }

    await _firestore.collection('users').doc(uid).set(data);

    // Load lại profile cho chắc
    try {
      final doc = await _firestore.collection('users').doc(uid).get();
      _userProfile = doc.data();
    } catch (e) {
      debugPrint('Error reloading user profile after register: $e');
    }

    notifyListeners();
    // authStateChanges cũng sẽ tự chạy, nhưng mình notify trước cho UI update nhanh
  }

  Future<void> updateAvatar(String url) async {
    if (user == null) return;
    await _firestore.collection('users').doc(user!.uid).update({
      'avatarUrl': url,
    });
    _userProfile ??= {};
    _userProfile!['avatarUrl'] = url;
    notifyListeners();
  }

  Future<void> logout() async {
    await _auth.signOut();
  }
}
