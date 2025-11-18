class Book {
  final int id;
  final String title;
  final String author;
  final String description;
  final String coverUrl; // có thể là URL http hoặc asset path
  final String assetPath; // đường dẫn file nội dung trong assets

  Book({
    required this.id,
    required this.title,
    required this.author,
    required this.description,
    required this.coverUrl,
    required this.assetPath,
  });

  factory Book.fromJson(Map<String, dynamic> json) {
    return Book(
      id: json['id'] as int,
      title: json['title'] as String,
      author: json['author'] as String? ?? '',
      description: json['description'] as String? ?? '',
      coverUrl: json['coverUrl'] as String? ?? '',
      assetPath: json['assetPath'] as String,
    );
  }
}
