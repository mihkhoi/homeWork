import 'package:flutter/foundation.dart';
import '../../../domain/entities/restaurant.dart';
import '../../../domain/repositories/restaurant_repository.dart';

class RestaurantProvider extends ChangeNotifier {
  final RestaurantRepository repo;
  RestaurantProvider(this.repo) {
    _init();
  }

  List<Restaurant> items = [];

  void _init() {
    repo.createDemoIfEmpty();
    repo.watchAll().listen((list) {
      items = list;
      notifyListeners();
    });
  }
}
