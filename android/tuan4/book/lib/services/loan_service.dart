import 'package:cloud_firestore/cloud_firestore.dart';

class LoanService {
  final CollectionReference loansRef = FirebaseFirestore.instance.collection(
    'loans',
  );
  final CollectionReference booksRef = FirebaseFirestore.instance.collection(
    'books',
  );

  /// User mượn sách:
  /// - Đếm số lượt mượn đang mở (status = borrowing) của cuốn đó
  /// - Nếu < totalCopies -> cho mượn, cập nhật availableCopies = total - số_đang_mượn_mới
  Future<void> borrowBook({
    required String userId,
    required String bookId,
  }) async {
    final now = DateTime.now();
    final due = now.add(const Duration(days: 7)); // hạn trả 7 ngày (tùy bạn)

    final bookDocRef = booksRef.doc(bookId);
    final bookSnap = await bookDocRef.get();
    if (!bookSnap.exists) {
      throw Exception('Sách không tồn tại');
    }

    final bookData = bookSnap.data() as Map<String, dynamic>;
    final int total = (bookData['totalCopies'] ?? 0) as int;

    // Lấy tất cả loan của cuốn này (chỉ where 1 field để khỏi cần index)
    final loansSnap = await loansRef.where('bookId', isEqualTo: bookId).get();

    // Đếm số loan đang mượn
    final int borrowingCount = loansSnap.docs.where((doc) {
      final data = doc.data() as Map<String, dynamic>;
      return data['status'] == 'borrowing';
    }).length;

    // Nếu đã đủ số bản cho mượn -> chặn
    if (borrowingCount >= total) {
      throw Exception('Sách đã hết, không thể mượn');
    }

    // Sau khi mượn thêm 1 bản:
    final int newBorrowingCount = borrowingCount + 1;
    final int newAvailable = total - newBorrowingCount;

    await FirebaseFirestore.instance.runTransaction((transaction) async {
      // Cập nhật lại sách theo số lượng đang mượn thực tế
      transaction.update(bookDocRef, {
        'availableCopies': newAvailable,
        'isAvailable': newAvailable > 0,
      });

      // Tạo loan mới
      transaction.set(loansRef.doc(), {
        'userId': userId,
        'bookId': bookId,
        'borrowDate': now,
        'dueDate': due,
        'returnDate': null,
        'status': 'borrowing',
      });
    });
  }
}
