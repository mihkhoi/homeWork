import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/book_provider.dart';
import '../../models/book.dart';
import 'book_detail_screen.dart';

class BookListScreen extends StatelessWidget {
  const BookListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final bookProvider = context.watch<BookProvider>();

    if (bookProvider.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    final List<Book> books = bookProvider.books;

    if (books.isEmpty) {
      return const Center(child: Text('Chưa có sách nào trong thư viện'));
    }

    return ListView.builder(
      padding: const EdgeInsets.all(8),
      itemCount: books.length,
      itemBuilder: (context, index) {
        final book = books[index];

        return Card(
          margin: const EdgeInsets.symmetric(vertical: 6),
          child: ListTile(
            leading: book.coverUrl.isNotEmpty
                ? ClipRRect(
                    borderRadius: BorderRadius.circular(4),
                    child: Image.network(
                      book.coverUrl,
                      width: 40,
                      height: 60,
                      fit: BoxFit.cover,
                    ),
                  )
                : const Icon(Icons.menu_book),
            title: Text(book.title),
            subtitle: Text(
              book.author.isEmpty ? 'Không rõ tác giả' : book.author,
            ),
            trailing: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  book.isAvailable ? 'Còn sách' : 'Hết sách',
                  style: TextStyle(
                    color: book.isAvailable ? Colors.green : Colors.red,
                    fontSize: 12,
                  ),
                ),
                Text(
                  'Còn: ${book.availableCopies}/${book.totalCopies}',
                  style: const TextStyle(fontSize: 11),
                ),
              ],
            ),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => BookDetailScreen(book: book)),
              );
            },
          ),
        );
      },
    );
  }
}
