import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firebase_storage/firebase_storage.dart'; // ❌ không cần nữa

import '../models/restaurant.dart';
import '../providers/auth_provider.dart';
import '../services/restaurant_service.dart';
import '../widgets/rating_stars.dart';
import 'login_screen.dart';
import 'restaurant_detail_screen.dart';
import 'add_restaurant_screen.dart';

class RestaurantListScreen extends StatelessWidget {
  const RestaurantListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();

    // service dùng chung cho cả màn (chỉ cần Firestore)
    final service = RestaurantService(FirebaseFirestore.instance);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Danh sách nhà hàng'),
        actions: [
          // nút thêm nhà hàng
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              // nếu chưa login thì bắt login trước
              if (auth.user == null) {
                Navigator.of(
                  context,
                ).push(MaterialPageRoute(builder: (_) => const LoginScreen()));
              } else {
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (_) => AddRestaurantScreen(service: service),
                  ),
                );
              }
            },
          ),
          // nút logout (chỉ hiện khi đã đăng nhập)
          if (auth.user != null)
            IconButton(
              icon: const Icon(Icons.logout),
              onPressed: () async {
                await auth.logout();
                if (context.mounted) {
                  Navigator.of(context).pushAndRemoveUntil(
                    MaterialPageRoute(builder: (_) => const LoginScreen()),
                    (_) => false,
                  );
                }
              },
            ),
        ],
      ),
      body: StreamBuilder<List<Restaurant>>(
        stream: service.restaurantsStream(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(child: Text('Lỗi: ${snapshot.error}'));
          }

          final restaurants = snapshot.data ?? [];
          if (restaurants.isEmpty) {
            return const Center(child: Text('Chưa có nhà hàng'));
          }

          return ListView.builder(
            itemCount: restaurants.length,
            itemBuilder: (_, index) {
              final r = restaurants[index];
              return ListTile(
                leading: r.imageUrl.isNotEmpty
                    ? Image.network(r.imageUrl, width: 60, fit: BoxFit.cover)
                    : const Icon(Icons.restaurant),
                title: Text(r.name),
                subtitle: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(r.address),
                    Row(
                      children: [
                        RatingStars(rating: r.avgRating),
                        const SizedBox(width: 4),
                        Text('(${r.ratingCount})'),
                      ],
                    ),
                  ],
                ),
                isThreeLine: true,
                onTap: () {
                  Navigator.of(context).push(
                    MaterialPageRoute(
                      builder: (_) => RestaurantDetailScreen(
                        restaurant: r,
                        service: service,
                      ),
                    ),
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
