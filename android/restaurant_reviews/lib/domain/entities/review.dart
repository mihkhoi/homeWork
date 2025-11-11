class Review {
  final String id;
  final String userId;
  final String userName;
  final String? userAvatar;
  final int rating;
  final String content;
  final String? imageUrl;
  final DateTime createdAt;

  const Review({
    required this.id,
    required this.userId,
    required this.userName,
    this.userAvatar,
    required this.rating,
    required this.content,
    this.imageUrl,
    required this.createdAt,
  });

  factory Review.fromMap(String id, Map<String, dynamic> m) => Review(
    id: id,
    userId: m['userId'] ?? '',
    userName: m['userName'] ?? '',
    userAvatar: m['userAvatar'],
    rating: (m['rating'] ?? 0) as int,
    content: m['content'] ?? '',
    imageUrl: m['imageUrl'],
    createdAt: DateTime.fromMillisecondsSinceEpoch(
      (m['createdAt'] ?? 0) as int,
    ),
  );

  Map<String, dynamic> toMap() => {
    'userId': userId,
    'userName': userName,
    'userAvatar': userAvatar,
    'rating': rating,
    'content': content,
    'imageUrl': imageUrl,
    'createdAt': createdAt.millisecondsSinceEpoch,
  }..removeWhere((k, v) => v == null);
}
