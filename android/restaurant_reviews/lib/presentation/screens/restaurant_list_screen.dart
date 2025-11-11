import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../domain/repositories/auth_repository.dart';
import '../providers/auth_provider.dart';
import '../providers/restaurant_provider.dart';
import '../widgets/restaurant_card.dart';
import 'restaurant_detail_screen.dart';

class RestaurantListScreen extends StatelessWidget {
  const RestaurantListScreen({super.key});
  @override
  Widget build(BuildContext context) {
    final p = context.watch<RestaurantProvider>();
    final me = context.watch<AuthProvider>().current;
    return Scaffold(
      appBar: AppBar(
        title: const Text('Nhà hàng nổi bật'),
        actions: [
          if (me != null)
            CircleAvatar(
              backgroundImage: me.photoUrl == null
                  ? null
                  : NetworkImage(me.photoUrl!),
            ),
          const SizedBox(width: 12),
          IconButton(
            onPressed: () => context.read<AuthRepository>().signOut(),
            icon: const Icon(Icons.logout),
          ),
        ],
      ),
      body: ListView.builder(
        itemCount: p.items.length,
        itemBuilder: (_, i) {
          final r = p.items[i];
          return RestaurantCard(
            r: r,
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => RestaurantDetailScreen(r: r)),
              );
            },
          );
        },
      ),
    );
  }
}
