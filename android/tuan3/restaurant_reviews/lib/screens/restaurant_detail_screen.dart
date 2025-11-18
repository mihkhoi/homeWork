import 'package:flutter/material.dart';

import '../models/restaurant.dart';
import '../models/review.dart';
import '../services/restaurant_service.dart';
import '../widgets/rating_stars.dart';
import 'add_review_screen.dart';

class RestaurantDetailScreen extends StatelessWidget {
  final Restaurant restaurant;
  final RestaurantService service;

  const RestaurantDetailScreen({
    super.key,
    required this.restaurant,
    required this.service,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(restaurant.name)),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (_) =>
                  AddReviewScreen(restaurant: restaurant, service: service),
            ),
          );
        },
        child: const Icon(Icons.rate_review),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (restaurant.imageUrl.isNotEmpty)
            Image.network(
              restaurant.imageUrl,
              height: 200,
              width: double.infinity,
              fit: BoxFit.cover,
            ),
          Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  restaurant.name,
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: 4),
                Text(restaurant.address),
                const SizedBox(height: 8),
                Row(
                  children: [
                    RatingStars(rating: restaurant.avgRating, size: 20),
                    const SizedBox(width: 8),
                    Text(
                      '${restaurant.avgRating.toStringAsFixed(1)} '
                      '(${restaurant.ratingCount} đánh giá)',
                    ),
                  ],
                ),
              ],
            ),
          ),
          const Divider(),
          const Padding(
            padding: EdgeInsets.all(8),
            child: Text('Đánh giá gần đây'),
          ),
          Expanded(
            child: StreamBuilder<List<Review>>(
              stream: service.reviewsStream(restaurant.id),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (snapshot.hasError) {
                  return Center(child: Text('Lỗi: ${snapshot.error}'));
                }
                final reviews = snapshot.data ?? [];
                if (reviews.isEmpty) {
                  return const Center(child: Text('Chưa có đánh giá'));
                }

                return ListView.builder(
                  itemCount: reviews.length,
                  itemBuilder: (_, index) {
                    final rv = reviews[index];
                    return ListTile(
                      leading: rv.imageUrl.isNotEmpty
                          ? Image.network(
                              rv.imageUrl,
                              width: 56,
                              fit: BoxFit.cover,
                            )
                          : const Icon(Icons.person),
                      title: Text('${rv.userName} - ${rv.rating}⭐'),
                      subtitle: Text(rv.comment),
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
