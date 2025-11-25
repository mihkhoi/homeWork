import 'package:flutter/foundation.dart';

import '../models/book.dart';
import '../services/book_service.dart';

class BookProvider extends ChangeNotifier {
  final BookService _bookService;

  BookProvider(this._bookService) {
    _listenBooks();
  }

  List<Book> books = [];
  bool isLoading = true;

  void _listenBooks() {
    _bookService.streamBooks().listen((data) {
      books = data;
      isLoading = false;
      notifyListeners();
    });
  }

  Future<void> addBook(Book book) async {
    await _bookService.addBook(book);
  }

  Future<void> updateBook(String id, Map<String, dynamic> data) async {
    await _bookService.updateBook(id, data);
  }

  Future<void> deleteBook(String id) async {
    await _bookService.deleteBook(id);
  }
}
