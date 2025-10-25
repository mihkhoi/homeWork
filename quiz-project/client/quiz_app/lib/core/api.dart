import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class Api {
  static final Dio dio = Dio(BaseOptions(
    baseUrl: 'http://10.0.2.2:5000', // Android emulator -> host máy: 10.0.2.2
    // Nếu chạy thiết bị thật, đổi thành IP LAN của máy chạy API
  ));

  static Future<void> attachToken() async {
    final storage = FlutterSecureStorage();
    final token = await storage.read(key: 'jwt');
    dio.interceptors.clear();
    dio.interceptors.add(InterceptorsWrapper(
      onRequest: (o, h) {
        if (token != null) o.headers['Authorization'] = 'Bearer $token';
        return h.next(o);
      },
    ));
  }
}
