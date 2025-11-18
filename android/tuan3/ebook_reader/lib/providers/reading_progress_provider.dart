import 'package:flutter/foundation.dart';

import '../services/db_helper.dart';

class ReadingProgressProvider extends ChangeNotifier {
  final Map<int, int> _bookLastPage = {}; // bookId -> lastPage

  Future<int> getLastPage(int bookId) async {
    if (_bookLastPage.containsKey(bookId)) {
      return _bookLastPage[bookId]!;
    }
    final lastPage = await DbHelper.instance.getLastPage(bookId);
    _bookLastPage[bookId] = lastPage;
    return lastPage;
  }

  Future<void> updateLastPage(int bookId, int page) async {
    _bookLastPage[bookId] = page;
    notifyListeners();
    await DbHelper.instance.saveLastPage(bookId, page);
  }
}
