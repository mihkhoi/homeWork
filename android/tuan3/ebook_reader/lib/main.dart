import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'providers/books_provider.dart';
import 'providers/reading_settings_provider.dart';
import 'providers/reading_progress_provider.dart';
import 'screens/home_screen.dart';
import 'services/book_api_service.dart';

void main() {
  runApp(const EbookApp());
}

class EbookApp extends StatelessWidget {
  const EbookApp({super.key});

  @override
  Widget build(BuildContext context) {
    // Service mock: không cần baseUrl nữa
    final apiService = BookApiService();

    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => BooksProvider(apiService: apiService),
        ),
        ChangeNotifierProvider(
          create: (_) => ReadingSettingsProvider()..load(),
        ),
        ChangeNotifierProvider(create: (_) => ReadingProgressProvider()),
      ],
      child: Consumer<ReadingSettingsProvider>(
        builder: (context, settingsProvider, _) {
          if (!settingsProvider.initialized) {
            return const MaterialApp(
              home: Scaffold(body: Center(child: CircularProgressIndicator())),
            );
          }

          final isDark = settingsProvider.isDarkMode;

          return MaterialApp(
            title: 'Ebook Reader',
            debugShowCheckedModeBanner: false,
            theme: ThemeData(
              brightness: isDark ? Brightness.dark : Brightness.light,
            ),
            home: const HomeScreen(),
          );
        },
      ),
    );
  }
}
