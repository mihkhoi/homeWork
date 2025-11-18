import 'package:flutter/foundation.dart';

import '../models/book.dart';
import '../services/book_api_service.dart';

class BooksProvider extends ChangeNotifier {
  final BookApiService apiService;

  BooksProvider({required this.apiService});

  List<Book> _books = [];
  bool _loading = false;
  String? _error;

  List<Book> get books => _books;
  bool get loading => _loading;
  String? get error => _error;

  Future<void> loadBooks() async {
    _loading = true;
    _error = null;
    notifyListeners();

    try {
      _books = await apiService.fetchBooks();
    } catch (e) {
      _error = e.toString();
    } finally {
      _loading = false;
      notifyListeners();
    }
  }
}
