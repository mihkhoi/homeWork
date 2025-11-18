import 'package:cloud_firestore/cloud_firestore.dart';

class Restaurant {
  final String id;
  final String name;
  final String address;
  final String imageUrl;
  final double avgRating;
  final int ratingCount;

  Restaurant({
    required this.id,
    required this.name,
    required this.address,
    required this.imageUrl,
    required this.avgRating,
    required this.ratingCount,
  });

  factory Restaurant.fromDoc(DocumentSnapshot<Map<String, dynamic>> doc) {
    final data = doc.data() ?? {};
    return Restaurant(
      id: doc.id,
      name: data['name'] ?? '',
      address: data['address'] ?? '',
      imageUrl: data['imageUrl'] ?? '',
      avgRating: (data['avgRating'] ?? 0).toDouble(),
      ratingCount: (data['ratingCount'] ?? 0) as int,
    );
  }
}
