import 'dart:convert';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:image_picker/image_picker.dart';
import 'package:http/http.dart' as http;

import '../config/cloudinary_config.dart';
import '../models/restaurant.dart';
import '../models/review.dart';

class RestaurantService {
  final FirebaseFirestore _firestore;

  RestaurantService(this._firestore);

  // ====== COMMON UPLOAD TO CLOUDINARY ======

  Future<String> _uploadToCloudinary(
    XFile file, {
    required String folder,
  }) async {
    final url = Uri.parse(
      'https://api.cloudinary.com/v1_1/$cloudinaryCloudName/image/upload',
    );

    final bytes = await file.readAsBytes();

    final request = http.MultipartRequest('POST', url)
      ..fields['upload_preset'] = cloudinaryUploadPreset
      ..fields['folder'] = folder
      ..files.add(
        http.MultipartFile.fromBytes('file', bytes, filename: file.name),
      );

    final streamed = await request.send();
    final response = await http.Response.fromStream(streamed);

    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception(
        'Cloudinary upload failed: ${response.statusCode} ${response.body}',
      );
    }

    final data = json.decode(response.body) as Map<String, dynamic>;
    final secureUrl = data['secure_url'] as String?;
    if (secureUrl == null) {
      throw Exception('Cloudinary response missing secure_url');
    }

    return secureUrl;
  }

  // ====== NHÀ HÀNG ======

  // Stream danh sách nhà hàng (real-time)
  Stream<List<Restaurant>> restaurantsStream() {
    return _firestore
        .collection('restaurants')
        .orderBy('name')
        .snapshots()
        .map(
          (snapshot) =>
              snapshot.docs.map((doc) => Restaurant.fromDoc(doc)).toList(),
        );
  }

  // Thêm nhà hàng (có thể kèm ảnh file hoặc URL)
  Future<void> addRestaurant({
    required String name,
    required String address,
    String imageUrl = '',
    XFile? imageFile,
  }) async {
    final docRef = _firestore.collection('restaurants').doc();

    String finalImageUrl = imageUrl;

    // Nếu có chọn file ảnh thì upload lên Cloudinary
    if (imageFile != null) {
      finalImageUrl = await _uploadToCloudinary(
        imageFile,
        folder: 'restaurant_photos',
      );
    }

    await docRef.set({
      'name': name,
      'address': address,
      'imageUrl': finalImageUrl,
      'avgRating': 0,
      'ratingCount': 0,
      'createdAt': FieldValue.serverTimestamp(),
    });
  }

  // ====== REVIEW ======

  // Stream danh sách review cho 1 nhà hàng
  Stream<List<Review>> reviewsStream(String restaurantId) {
    return _firestore
        .collection('restaurants')
        .doc(restaurantId)
        .collection('reviews')
        .orderBy('createdAt', descending: true)
        .snapshots()
        .map(
          (snapshot) =>
              snapshot.docs.map((doc) => Review.fromDoc(doc)).toList(),
        );
  }

  // Thêm review, có thể kèm ảnh
  Future<void> addReview({
    required String restaurantId,
    required String userId,
    required String userName,
    required int rating,
    required String comment,
    XFile? imageFile,
  }) async {
    final reviewsRef = _firestore
        .collection('restaurants')
        .doc(restaurantId)
        .collection('reviews');

    final newDoc = reviewsRef.doc();
    String imageUrl = '';

    if (imageFile != null) {
      imageUrl = await _uploadToCloudinary(
        imageFile,
        folder: 'review_photos/$restaurantId',
      );
    }

    await newDoc.set({
      'userId': userId,
      'userName': userName,
      'rating': rating,
      'comment': comment,
      'imageUrl': imageUrl,
      'createdAt': FieldValue.serverTimestamp(),
    });
  }
}
