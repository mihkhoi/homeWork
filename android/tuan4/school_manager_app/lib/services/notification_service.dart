import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/foundation.dart';

class NotificationService {
  final FirebaseMessaging _messaging = FirebaseMessaging.instance;

  Future<void> init() async {
    // Xin quyền nhận thông báo (Android 13+, iOS)
    final settings = await _messaging.requestPermission();

    debugPrint('FCM permission: ${settings.authorizationStatus}');

    // Lấy FCM token (để lưu vào Firestore nếu cần)
    final token = await _messaging.getToken();
    debugPrint('FCM token: $token');

    // Lắng nghe notification khi app đang mở
    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      debugPrint('FCM onMessage: ${message.notification?.title}');
    });
  }
}
