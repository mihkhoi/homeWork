import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../models/book.dart';
import '../../providers/auth_provider.dart';

// Đặt prefix cho từng file để tránh trùng tên
import '../../services/loan_service.dart' as loan_service;
import '../../screens/librarian/add_edit_book_screen.dart' as librarian;

class BookDetailScreen extends StatelessWidget {
  final Book book;

  const BookDetailScreen({super.key, required this.book});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final isLibrarian = auth.isLibrarian;

    return Scaffold(
      appBar: AppBar(title: Text(book.title)),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Ảnh bìa
            Center(
              child: book.coverUrl.isNotEmpty
                  ? ClipRRect(
                      borderRadius: BorderRadius.circular(8),
                      child: Image.network(
                        book.coverUrl,
                        height: 220,
                        fit: BoxFit.cover,
                      ),
                    )
                  : const Icon(Icons.menu_book, size: 80),
            ),
            const SizedBox(height: 16),

            // Tiêu đề + tác giả
            Text(book.title, style: Theme.of(context).textTheme.titleLarge),
            const SizedBox(height: 4),
            Text(
              book.author.isEmpty ? 'Không rõ tác giả' : book.author,
              style: Theme.of(context).textTheme.bodyMedium,
            ),

            const SizedBox(height: 12),

            // Trạng thái
            Row(
              children: [
                Text(
                  book.isAvailable
                      ? 'Trạng thái: Còn sách'
                      : 'Trạng thái: Hết sách',
                  style: TextStyle(
                    color: book.isAvailable ? Colors.green : Colors.red,
                  ),
                ),
                const SizedBox(width: 12),
                Text(
                  '(${book.availableCopies}/${book.totalCopies})',
                  style: const TextStyle(fontSize: 12),
                ),
              ],
            ),

            const SizedBox(height: 16),

            // Thể loại
            if (book.category.isNotEmpty)
              Text(
                'Thể loại: ${book.category}',
                style: Theme.of(context).textTheme.bodyMedium,
              ),

            const SizedBox(height: 16),

            // Mô tả
            Text('Mô tả:', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 4),
            Text(
              book.description.isEmpty
                  ? 'Chưa có mô tả cho sách này.'
                  : book.description,
            ),

            const SizedBox(height: 24),

            // Nút thao tác
            Row(
              children: [
                // Nút mượn sách
                Expanded(
                  child: ElevatedButton(
                    onPressed: book.isAvailable
                        ? () async {
                            final authRead = context.read<AuthProvider>();
                            final user = authRead.currentUser;

                            if (user == null) {
                              if (!context.mounted) return;
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(
                                  content: Text(
                                    'Vui lòng đăng nhập để mượn sách',
                                  ),
                                ),
                              );
                              return;
                            }

                            try {
                              await loan_service.LoanService().borrowBook(
                                userId: user.uid,
                                bookId: book.id,
                              );

                              if (!context.mounted) return;
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(
                                  content: Text('Mượn sách thành công'),
                                ),
                              );
                            } catch (e) {
                              if (!context.mounted) return;
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(
                                  content: Text('Không thể mượn sách: $e'),
                                ),
                              );
                            }
                          }
                        : null,
                    child: const Text('Mượn sách'),
                  ),
                ),

                // Nút sửa chỉ hiện cho thủ thư
                if (isLibrarian) const SizedBox(width: 12),
                if (isLibrarian)
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) =>
                                librarian.AddEditBookScreen(book: book),
                          ),
                        );
                      },
                      child: const Text('Sửa (Thủ thư)'),
                    ),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
