import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:provider/provider.dart';

import 'config/firebase_options.dart';
import 'providers/auth_provider.dart';
import 'services/auth_service.dart';

// THÊM 2 IMPORT NÀY
import 'providers/book_provider.dart';
import 'services/book_service.dart';

import 'screens/auth/login_screen.dart';
import 'screens/home/home_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);

  runApp(const LibraryApp());
}

class LibraryApp extends StatelessWidget {
  const LibraryApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        // Provider quản lý đăng nhập + role
        ChangeNotifierProvider(create: (_) => AuthProvider(AuthService())),

        // Provider quản lý sách
        ChangeNotifierProvider(create: (_) => BookProvider(BookService())),
      ],
      child: MaterialApp(
        title: 'Quản lý thư viện',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(useMaterial3: true),
        home: const RootScreen(),
      ),
    );
  }
}

class RootScreen extends StatelessWidget {
  const RootScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final authProvider = context.watch<AuthProvider>();

    // Chỉ quay vòng tròn khi APP mới mở,
    // đang chờ Firebase trả trạng thái đăng nhập ban đầu
    if (authProvider.isInitializing) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    // Nếu chưa đăng nhập -> về màn Login
    if (authProvider.currentUser == null) {
      return const LoginScreen();
    }

    // Đã đăng nhập -> vào HomeScreen (có phân role trong HomeScreen)
    return const HomeScreen();
  }
}
