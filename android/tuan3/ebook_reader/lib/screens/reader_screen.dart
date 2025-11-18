import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

import '../models/book.dart';
import '../providers/reading_progress_provider.dart';
import '../providers/reading_settings_provider.dart';

class ReaderScreen extends StatefulWidget {
  final Book book;

  const ReaderScreen({super.key, required this.book});

  @override
  State<ReaderScreen> createState() => _ReaderScreenState();
}

class _ReaderScreenState extends State<ReaderScreen> {
  final PageController _pageController = PageController();
  List<String> _pages = [];
  bool _loading = true;
  int _currentPage = 0;
  bool _showControls = true;

  @override
  void initState() {
    super.initState();
    _initReader();
  }

  Future<void> _initReader() async {
    // Lấy provider TRƯỚC khi có await (tránh dùng context qua async gap)
    final progressProvider = context.read<ReadingProgressProvider>();

    // đọc nội dung file .txt từ assets
    final text = await rootBundle.loadString(widget.book.assetPath);

    // Cách đơn giản: tách theo 2 dòng trống thành 1 "trang"
    final pages = text.split('\n\n');

    // lấy trang đã đọc trước đó từ DB
    final lastPage = await progressProvider.getLastPage(widget.book.id);

    if (!mounted) return; // đảm bảo widget chưa bị dispose

    setState(() {
      _pages = pages;
      _currentPage = (lastPage >= 0 && lastPage < pages.length) ? lastPage : 0;
      _loading = false;
    });

    // Nhảy tới trang đã đọc sau khi frame đã build xong
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (!_pageController.hasClients) return;
      _pageController.jumpToPage(_currentPage);
    });
  }

  void _toggleControls() {
    setState(() {
      _showControls = !_showControls;
    });
  }

  Future<void> _onPageChanged(int index) async {
    setState(() {
      _currentPage = index;
    });

    // Lấy provider ra trước, rồi mới await (tránh warning context)
    final progressProvider = context.read<ReadingProgressProvider>();
    await progressProvider.updateLastPage(widget.book.id, index);
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<ReadingSettingsProvider>().settings;

    if (_loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    final isDark = settings.isDarkMode;

    return Scaffold(
      backgroundColor: isDark ? Colors.black : Colors.white,
      appBar: AppBar(
        backgroundColor: isDark ? Colors.black : Colors.white,
        elevation: 0,
        iconTheme: IconThemeData(color: isDark ? Colors.white : Colors.black),
        title: Text(
          widget.book.title,
          style: TextStyle(color: isDark ? Colors.white : Colors.black),
        ),
      ),
      body: SafeArea(
        child: GestureDetector(
          onTap: _toggleControls,
          child: Stack(
            children: [
              // ----------- NỘI DUNG SÁCH -----------
              PageView.builder(
                controller: _pageController,
                itemCount: _pages.length,
                onPageChanged: _onPageChanged,
                itemBuilder: (_, index) {
                  return SingleChildScrollView(
                    padding: const EdgeInsets.all(16),
                    child: Text(
                      _pages[index],
                      style: TextStyle(
                        fontSize: settings.fontSize,
                        color: isDark ? Colors.white : Colors.black,
                      ),
                    ),
                  );
                },
              ),

              // ----------- THANH ĐIỀU KHIỂN -----------
              Positioned(
                left: 0,
                right: 0,
                bottom: 0,
                child: AnimatedContainer(
                  duration: const Duration(milliseconds: 300),
                  height: _showControls ? 120 : 0,
                  padding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 8,
                  ),
                  decoration: BoxDecoration(
                    color: isDark
                        ? Colors.black.withValues(alpha: 0.8)
                        : Colors.white.withValues(alpha: 0.9),
                    boxShadow: [
                      BoxShadow(
                        blurRadius: 10,
                        offset: const Offset(0, -2),
                        color: Colors.black.withValues(alpha: 0.2),
                      ),
                    ],
                  ),
                  child: _showControls
                      ? _buildControls(context, settings)
                      : null,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildControls(BuildContext context, dynamic settings) {
    final settingsProvider = context.read<ReadingSettingsProvider>();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // dòng 1: dark mode + current page
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Row(
              children: [
                const Text('Dark mode'),
                Switch(
                  value: settings.isDarkMode,
                  onChanged: (_) => settingsProvider.toggleDarkMode(),
                ),
              ],
            ),
            Text('Trang ${_currentPage + 1}/${_pages.length}'),
          ],
        ),
        const SizedBox(height: 8),

        // dòng 2: slider font size
        Row(
          children: [
            const Text('Font'),
            Expanded(
              child: Slider(
                value: settings.fontSize,
                min: 12,
                max: 28,
                onChanged: (value) {
                  settingsProvider.setFontSize(value);
                },
              ),
            ),
          ],
        ),
      ],
    );
  }
}
