import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:provider/provider.dart';

import '../../models/loan.dart';
import '../../models/book.dart';
import '../../providers/auth_provider.dart';

class LoanHistoryScreen extends StatelessWidget {
  const LoanHistoryScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final user = auth.currentUser;
    final isLibrarian = auth.isLibrarian;

    // THỦ THƯ: xem ai đang mượn sách
    // USER THƯỜNG: xem lịch sử của chính mình
    if (!isLibrarian && user == null) {
      return const Center(
        child: Text('Vui lòng đăng nhập để xem lịch sử mượn sách'),
      );
    }

    Query loansQuery = FirebaseFirestore.instance.collection('loans');

    if (isLibrarian) {
      // Thủ thư: xem các loan đang mượn
      loansQuery = loansQuery.where('status', isEqualTo: 'borrowing');
    } else {
      // User thường: chỉ xem loan của chính mình
      loansQuery = loansQuery.where('userId', isEqualTo: user!.uid);
    }

    // KHÔNG orderBy -> không cần index
    final loansStream = loansQuery.snapshots();

    return StreamBuilder<QuerySnapshot>(
      stream: loansStream,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        }

        if (snapshot.hasError) {
          return const Center(child: Text('Lỗi khi tải lịch sử, thử lại sau'));
        }

        if (!snapshot.hasData || snapshot.data!.docs.isEmpty) {
          return Center(
            child: Text(
              isLibrarian
                  ? 'Hiện không có ai đang mượn sách'
                  : 'Bạn chưa mượn cuốn sách nào',
            ),
          );
        }

        final loans = snapshot.data!.docs
            .map(
              (doc) => Loan.fromDoc(doc.id, doc.data() as Map<String, dynamic>),
            )
            .toList();

        // sort mới -> cũ
        loans.sort((a, b) => b.borrowDate.compareTo(a.borrowDate));

        return ListView.builder(
          padding: const EdgeInsets.all(8),
          itemCount: loans.length,
          itemBuilder: (context, index) {
            final loan = loans[index];
            return _LoanItem(loan: loan, isLibrarian: isLibrarian);
          },
        );
      },
    );
  }
}

class _LoanItem extends StatelessWidget {
  final Loan loan;
  final bool isLibrarian;

  const _LoanItem({required this.loan, required this.isLibrarian});

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  @override
  Widget build(BuildContext context) {
    final bookRef = FirebaseFirestore.instance
        .collection('books')
        .doc(loan.bookId);

    // nếu là thủ thư, lấy thêm user
    final userRef = FirebaseFirestore.instance
        .collection('users')
        .doc(loan.userId);

    return Card(
      margin: const EdgeInsets.symmetric(vertical: 6),
      child: ListTile(
        title: FutureBuilder<DocumentSnapshot>(
          future: bookRef.get(),
          builder: (context, snapshot) {
            if (!snapshot.hasData) {
              return const Text('Đang tải thông tin sách...');
            }
            if (!snapshot.data!.exists) {
              return const Text('Sách đã bị xóa');
            }

            final data = snapshot.data!.data() as Map<String, dynamic>;
            final book = Book.fromDoc(snapshot.data!.id, data);

            return Text(book.title);
          },
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (isLibrarian)
              FutureBuilder<DocumentSnapshot>(
                future: userRef.get(),
                builder: (context, snap) {
                  if (!snap.hasData || !snap.data!.exists) {
                    return const Text('Người mượn: (không xác định)');
                  }
                  final data = snap.data!.data() as Map<String, dynamic>;
                  final name = data['displayName'] ?? data['email'] ?? '';
                  return Text('Người mượn: $name');
                },
              ),
            Text('Ngày mượn: ${_formatDate(loan.borrowDate)}'),
            Text('Hạn trả: ${_formatDate(loan.dueDate)}'),
            if (loan.returnDate != null)
              Text('Ngày trả: ${_formatDate(loan.returnDate!)}'),
          ],
        ),
      ),
    );
  }
}
