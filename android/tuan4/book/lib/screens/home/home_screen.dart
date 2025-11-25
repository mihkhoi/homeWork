import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/auth_provider.dart';
import '../books/book_list_screen.dart';
import '../loans/loan_history_screen.dart';
import '../librarian/manage_books_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();

    final isLibrarian = auth.isLibrarian;

    // Với người dùng thường
    final userTabs = [const BookListScreen(), const LoanHistoryScreen()];

    // Với thủ thư (thêm tab quản lý sách)
    final librarianTabs = [
      const BookListScreen(),
      const LoanHistoryScreen(),
      const ManageBooksScreen(),
    ];

    final tabs = isLibrarian ? librarianTabs : userTabs;

    return Scaffold(
      appBar: AppBar(
        title: Text(isLibrarian ? 'Thư viện (Thủ thư)' : 'Thư viện'),
        actions: [
          IconButton(
            onPressed: () => auth.logout(),
            icon: const Icon(Icons.logout),
          ),
        ],
      ),
      body: tabs[_currentIndex],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentIndex,
        onTap: (value) => setState(() => _currentIndex = value),
        items: [
          const BottomNavigationBarItem(
            icon: Icon(Icons.menu_book),
            label: 'Sách',
          ),
          const BottomNavigationBarItem(
            icon: Icon(Icons.history),
            label: 'Lịch sử',
          ),
          if (isLibrarian)
            const BottomNavigationBarItem(
              icon: Icon(Icons.admin_panel_settings),
              label: 'Quản lý',
            ),
        ],
      ),
    );
  }
}
