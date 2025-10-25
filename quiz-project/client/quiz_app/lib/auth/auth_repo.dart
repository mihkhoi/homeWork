import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import '../core/api.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class AuthRepo {
  final _store = const FlutterSecureStorage();

  Future<bool> login(String u, String p) async {
    try {
      final res = await Api.dio.post('/auth/login', data: {'username': u, 'password': p});
      debugPrint('LOGIN OK status=${res.statusCode} data=$res');
      final token = res.data['token'] as String?;
      if (token == null) return false;
      await _store.write(key: 'jwt', value: token);
      await Api.attachToken();
      return true;
    } on DioError catch (e) {
      debugPrint('LOGIN ERROR status=${e.response?.statusCode} data=${e.response?.data} err=${e.message}');
      return false;
    }
  }
}
