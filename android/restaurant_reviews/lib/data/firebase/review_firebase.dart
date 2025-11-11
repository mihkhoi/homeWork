import 'package:cloud_firestore/cloud_firestore.dart';
import '../../domain/entities/review.dart';
import '../../domain/repositories/review_repository.dart';

class ReviewFirebase implements ReviewRepository {
  final db = FirebaseFirestore.instance;

  CollectionReference<Map<String, dynamic>> _col(String restaurantId) =>
      db.collection('restaurants').doc(restaurantId).collection('reviews');

  @override
  Stream<List<Review>> watchByRestaurant(String restaurantId) =>
      _col(restaurantId)
          .orderBy('createdAt', descending: true)
          .snapshots()
          .map(
            (s) => s.docs.map((d) => Review.fromMap(d.id, d.data())).toList(),
          );

  @override
  Future<void> addReview(String restaurantId, Review review) async {
    await _col(restaurantId).doc(review.id).set(review.toMap());

    // cập nhật rating trung bình
    final agg = await _col(restaurantId).get();
    final total = agg.docs.fold<int>(
      0,
      (a, b) => a + ((b.data()['rating'] ?? 0) as int),
    );
    final count = agg.docs.length;
    final avg = count == 0 ? 0 : total / count;
    await db.collection('restaurants').doc(restaurantId).update({
      'ratingAvg': double.parse(avg.toStringAsFixed(2)),
      'ratingCount': count,
    });
  }
}
