class Book {
  final String id;
  final String title;
  final String author;
  final String description;
  final String category;
  final String coverUrl;
  final bool isAvailable;
  final int totalCopies;
  final int availableCopies;

  Book({
    required this.id,
    required this.title,
    required this.author,
    required this.description,
    required this.category,
    required this.coverUrl,
    required this.isAvailable,
    required this.totalCopies,
    required this.availableCopies,
  });

  factory Book.fromDoc(String id, Map<String, dynamic> data) {
    return Book(
      id: id,
      title: data['title'] ?? '',
      author: data['author'] ?? '',
      description: data['description'] ?? '',
      category: data['category'] ?? '',
      coverUrl: data['coverUrl'] ?? '',
      isAvailable: data['isAvailable'] ?? true,
      totalCopies: data['totalCopies'] ?? 1,
      availableCopies: data['availableCopies'] ?? 0,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'title': title,
      'author': author,
      'description': description,
      'category': category,
      'coverUrl': coverUrl,
      'isAvailable': isAvailable,
      'totalCopies': totalCopies,
      'availableCopies': availableCopies,
    };
  }
}
