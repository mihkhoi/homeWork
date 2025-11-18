import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../providers/books_provider.dart';
import '../screens/reader_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    // Gọi loadBooks trực tiếp trong initState
    context.read<BooksProvider>().loadBooks();
  }

  @override
  Widget build(BuildContext context) {
    final booksProvider = context.watch<BooksProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Thư viện sách')),
      body: Builder(
        builder: (_) {
          if (booksProvider.loading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (booksProvider.error != null) {
            return Center(child: Text('Lỗi: ${booksProvider.error}'));
          }

          final books = booksProvider.books;
          if (books.isEmpty) {
            return const Center(child: Text('Không có sách'));
          }

          return ListView.builder(
            itemCount: books.length,
            itemBuilder: (_, index) {
              final book = books[index];

              return ListTile(
                leading: book.coverUrl.isNotEmpty
                    ? Image.asset(
                        book.coverUrl, // ví dụ: assets/images/book1_cover.jpg
                        width: 40,
                        fit: BoxFit.cover,
                      )
                    : const Icon(Icons.book),
                title: Text(book.title),
                subtitle: Text(book.author),
                onTap: () {
                  Navigator.of(context).push(
                    MaterialPageRoute(builder: (_) => ReaderScreen(book: book)),
                  );
                },
              );
            },
          );
        },
      ),
    );
  }
}
