import 'package:cloud_firestore/cloud_firestore.dart';
import '../../domain/entities/restaurant.dart';
import '../../domain/repositories/restaurant_repository.dart';

class RestaurantFirebase implements RestaurantRepository {
  final col = FirebaseFirestore.instance.collection('restaurants');

  @override
  Stream<List<Restaurant>> watchAll() => col
      .orderBy('ratingAvg', descending: true)
      .snapshots()
      .map(
        (s) => s.docs.map((d) => Restaurant.fromMap(d.id, d.data())).toList(),
      );

  @override
  Future<void> createDemoIfEmpty() async {
    final got = await col.limit(1).get();
    if (got.docs.isNotEmpty) return;
    await col.add({
      'name': 'Phở Ngon 24h',
      'address': '12 Lê Lợi, Q.1, TP.HCM',
      'photoUrl': '',
      'ratingAvg': 4.6,
      'ratingCount': 132,
    });
    await col.add({
      'name': 'Bún Chả Hà Nội',
      'address': '88 Tràng Tiền, Hoàn Kiếm, HN',
      'photoUrl': '',
      'ratingAvg': 4.4,
      'ratingCount': 98,
    });
  }
}
