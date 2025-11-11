import '../entities/restaurant.dart';

abstract class RestaurantRepository {
  Stream<List<Restaurant>> watchAll();
  Future<void> createDemoIfEmpty();
}
