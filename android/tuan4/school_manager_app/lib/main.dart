import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:provider/provider.dart';

import 'firebase_options.dart'; // do flutterfire configure tạo
import 'providers/auth_provider.dart';
import 'screens/login_screen.dart';
import 'screens/student_home_screen.dart';
import 'screens/parent_home_screen.dart';
import 'screens/teacher_home_screen.dart';
import 'services/notification_service.dart'; // FCM

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Khởi tạo Firebase
  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);

  // Khởi tạo Firebase Cloud Messaging (xin quyền, lấy token...)
  await NotificationService().init();

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => AuthProvider(),
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'School Manager',
        theme: ThemeData(
          useMaterial3: true,
          colorSchemeSeed: Colors.deepPurple,
        ),
        home: const RootScreen(),
      ),
    );
  }
}

/// Quyết định hiển thị màn nào dựa trên trạng thái Auth
class RootScreen extends StatelessWidget {
  const RootScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();

    // Lần đầu app mở, đang chờ Firebase/AuthProvider load profile
    if (auth.isInitializing) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    // Chưa đăng nhập -> màn Login
    if (auth.user == null) {
      return const LoginScreen();
    }

    // Đã đăng nhập -> xem role trong Firestore
    final role = auth.userProfile?['role'] as String? ?? 'student';

    if (role == 'teacher') {
      return const TeacherHomeScreen();
    } else if (role == 'parent') {
      return const ParentHomeScreen();
    } else {
      // mặc định: học sinh
      return const StudentHomeScreen();
    }
  }
}
