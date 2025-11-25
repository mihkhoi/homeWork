import 'package:cloud_firestore/cloud_firestore.dart';

class LoanService {
  final FirebaseFirestore _db = FirebaseFirestore.instance;

  Future<void> borrowBook({
    required String userId,
    required String bookId,
  }) async {
    final now = DateTime.now();
    final due = now.add(const Duration(days: 7)); // hạn trả 7 ngày

    await _db.runTransaction((tx) async {
      // -------- LẤY THÔNG TIN SÁCH --------
      final bookRef = _db.collection('books').doc(bookId);
      final bookSnap = await tx.get(bookRef);

      if (!bookSnap.exists) {
        throw Exception('Sách không tồn tại');
      }

      final data = bookSnap.data() as Map<String, dynamic>;
      final int total = (data['totalCopies'] ?? 0) as int;
      final int available =
          (data['availableCopies'] ?? total)
              as int; // fallback nếu chưa có field

      if (available <= 0) {
        throw Exception('Sách đã hết, không thể mượn');
      }

      // -------- TẠO LOAN MỚI --------
      final loanRef = _db.collection('loans').doc(); // /loans/{loanId}

      tx.set(loanRef, {
        'userId': userId, // PHẢI có, để trùng với auth.uid
        'bookId': bookId,
        'borrowDate': Timestamp.fromDate(now),
        'dueDate': Timestamp.fromDate(due),
        'returnDate': null,
        'status': 'borrowing',
      });

      // -------- CẬP NHẬT LẠI SÁCH --------
      final newAvailable = available - 1;

      tx.update(bookRef, {
        'availableCopies': newAvailable,
        'isAvailable': newAvailable > 0,
      });
    });
  }
}
