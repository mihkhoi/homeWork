import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/book_provider.dart';
// Dùng alias cho màn thêm/sửa sách
import '../../screens/librarian/add_edit_book_screen.dart' as librarian;

class ManageBooksScreen extends StatelessWidget {
  const ManageBooksScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final bookProvider = context.watch<BookProvider>();

    if (bookProvider.isLoading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    final books = bookProvider.books;

    return Scaffold(
      appBar: AppBar(title: const Text('Quản lý sách')),
      body: ListView.builder(
        itemCount: books.length,
        itemBuilder: (context, index) {
          final book = books[index];
          return ListTile(
            leading: book.coverUrl.isNotEmpty
                ? Image.network(
                    book.coverUrl,
                    width: 40,
                    height: 60,
                    fit: BoxFit.cover,
                  )
                : const Icon(Icons.menu_book),
            title: Text(book.title),
            subtitle: Text(book.author),
            trailing: PopupMenuButton<String>(
              onSelected: (value) {
                if (value == 'edit') {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => librarian.AddEditBookScreen(book: book),
                    ),
                  );
                } else if (value == 'delete') {
                  bookProvider.deleteBook(book.id);
                }
              },
              itemBuilder: (context) => const [
                PopupMenuItem(value: 'edit', child: Text('Sửa')),
                PopupMenuItem(value: 'delete', child: Text('Xóa')),
              ],
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => const librarian.AddEditBookScreen(),
            ),
          );
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
