class ReadingProgress {
  final int bookId;
  final int lastPage;

  ReadingProgress({required this.bookId, required this.lastPage});

  Map<String, dynamic> toMap() => {'bookId': bookId, 'lastPage': lastPage};

  factory ReadingProgress.fromMap(Map<String, dynamic> map) {
    return ReadingProgress(
      bookId: map['bookId'] as int,
      lastPage: map['lastPage'] as int,
    );
  }
}
