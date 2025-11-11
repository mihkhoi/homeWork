import '../entities/review.dart';

abstract class ReviewRepository {
  Stream<List<Review>> watchByRestaurant(String restaurantId);
  Future<void> addReview(String restaurantId, Review review);
}
