import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import '../../../domain/entities/restaurant.dart';

class RestaurantCard extends StatelessWidget {
  final Restaurant r;
  final VoidCallback? onTap;
  const RestaurantCard({super.key, required this.r, this.onTap});
  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      child: ListTile(
        leading: ClipRRect(
          borderRadius: BorderRadius.circular(8),
          child: r.photoUrl.isEmpty
              ? Image.asset(
                  'assets/placeholder.png',
                  width: 56,
                  height: 56,
                  fit: BoxFit.cover,
                )
              : CachedNetworkImage(
                  imageUrl: r.photoUrl,
                  width: 56,
                  height: 56,
                  fit: BoxFit.cover,
                ),
        ),
        title: Text(r.name),
        subtitle: Text('${r.address}\n⭐ ${r.ratingAvg} (${r.ratingCount})'),
        isThreeLine: true,
        onTap: onTap,
      ),
    );
  }
}
