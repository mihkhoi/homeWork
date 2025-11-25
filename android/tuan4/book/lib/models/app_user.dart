class AppUser {
  final String uid;
  final String email;
  final String displayName;
  final String role; // 'user' hoặc 'librarian'

  AppUser({
    required this.uid,
    required this.email,
    required this.displayName,
    required this.role,
  });

  factory AppUser.fromMap(String uid, Map<String, dynamic> data) {
    return AppUser(
      uid: uid,
      email: data['email'] ?? '',
      displayName: data['displayName'] ?? '',
      role: data['role'] ?? 'user',
    );
  }

  Map<String, dynamic> toMap() {
    return {'email': email, 'displayName': displayName, 'role': role};
  }
}
