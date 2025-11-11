import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

import 'domain/repositories/auth_repository.dart';
import 'domain/repositories/restaurant_repository.dart';
import 'domain/repositories/review_repository.dart';

import 'data/firebase/auth_firebase.dart';
import 'data/firebase/restaurant_firebase.dart';
import 'data/firebase/review_firebase.dart';

import 'presentation/providers/auth_provider.dart';
import 'presentation/providers/restaurant_provider.dart';
import 'presentation/providers/review_provider.dart';
import 'presentation/screens/sign_in_screen.dart';
import 'presentation/screens/restaurant_list_screen.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        Provider<AuthRepository>(create: (_) => AuthFirebase()),
        Provider<RestaurantRepository>(create: (_) => RestaurantFirebase()),
        Provider<ReviewRepository>(create: (_) => ReviewFirebase()),
        ChangeNotifierProvider(
          create: (c) => AuthProvider(c.read<AuthRepository>()),
        ),
        ChangeNotifierProvider(
          create: (c) => RestaurantProvider(c.read<RestaurantRepository>()),
        ),
        ChangeNotifierProvider(
          create: (c) => ReviewProvider(c.read<ReviewRepository>()),
        ),
      ],
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Đánh giá Nhà hàng',
        theme: ThemeData(
          useMaterial3: true,
          colorSchemeSeed: Colors.deepOrange,
        ),
        home: const Root(),
      ),
    );
  }
}

class Root extends StatelessWidget {
  const Root({super.key});
  @override
  Widget build(BuildContext context) {
    final me = context.watch<AuthProvider>().current;
    if (me == null) return const SignInScreen();
    return const RestaurantListScreen();
  }
}
