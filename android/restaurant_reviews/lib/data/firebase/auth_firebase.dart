import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '../../domain/entities/user_profile.dart';
import '../../domain/repositories/auth_repository.dart';

class AuthFirebase implements AuthRepository {
  final _auth = FirebaseAuth.instance;

  UserProfile _map(User u) =>
      UserProfile(uid: u.uid, name: u.displayName, photoUrl: u.photoURL);

  @override
  Stream<UserProfile?> authState() =>
      _auth.authStateChanges().map((u) => u == null ? null : _map(u));

  @override
  Future<UserProfile?> signInWithEmail(String email, String password) async {
    final cred = await _auth.signInWithEmailAndPassword(
      email: email,
      password: password,
    );
    final u = cred.user;
    return u == null ? null : _map(u);
  }

  @override
  Future<UserProfile?> registerWithEmail(
    String email,
    String password, {
    String? displayName,
  }) async {
    final cred = await _auth.createUserWithEmailAndPassword(
      email: email,
      password: password,
    );
    final u = cred.user;
    if (u == null) return null;
    if (displayName != null && displayName.isNotEmpty) {
      await u.updateDisplayName(displayName);
      await u.reload();
    }
    return _map(_auth.currentUser ?? u);
  }

  @override
  Future<void> sendPasswordReset(String email) =>
      _auth.sendPasswordResetEmail(email: email);

  @override
  Future<UserProfile?> signInWithGoogle() async {
    final g = await GoogleSignIn().signIn();
    if (g == null) return null;
    final ga = await g.authentication;
    final cred = await _auth.signInWithCredential(
      GoogleAuthProvider.credential(
        accessToken: ga.accessToken,
        idToken: ga.idToken,
      ),
    );
    final u = cred.user;
    return u == null ? null : _map(u);
  }

  @override
  Future<void> signOut() => _auth.signOut();
}
