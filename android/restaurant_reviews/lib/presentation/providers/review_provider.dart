import 'package:flutter/foundation.dart';
import 'package:uuid/uuid.dart';
import '../../../domain/entities/review.dart';
import '../../../domain/repositories/review_repository.dart';

class ReviewProvider extends ChangeNotifier {
  final ReviewRepository repo;
  ReviewProvider(this.repo);

  List<Review> items = [];

  void watch(String restaurantId) {
    repo.watchByRestaurant(restaurantId).listen((list) {
      items = list;
      notifyListeners();
    });
  }

  Future<void> add(
    String restaurantId, {
    required String userId,
    required String userName,
    String? userAvatar,
    required int rating,
    required String content,
    String? imageUrl,
  }) async {
    final id = const Uuid().v4();
    final r = Review(
      id: id,
      userId: userId,
      userName: userName,
      userAvatar: userAvatar,
      rating: rating,
      content: content,
      imageUrl: imageUrl,
      createdAt: DateTime.now(),
    );
    await repo.addReview(restaurantId, r);
  }
}
