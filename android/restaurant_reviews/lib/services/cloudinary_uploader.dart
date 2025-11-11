import 'dart:convert';
import 'dart:typed_data';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';

class CloudinaryUploader {
  final String cloudName;
  final String uploadPreset;

  const CloudinaryUploader({
    required this.cloudName,
    required this.uploadPreset,
  });

  Future<String> uploadBytes(
    Uint8List bytes, {
    String fileName = 'review.jpg',
    String mime = 'image/jpeg',
  }) async {
    final uri = Uri.parse(
      'https://api.cloudinary.com/v1_1/$cloudName/image/upload',
    );

    final req = http.MultipartRequest('POST', uri)
      ..fields['upload_preset'] = uploadPreset
      ..files.add(
        http.MultipartFile.fromBytes(
          'file',
          bytes,
          filename: fileName,
          contentType: _mediaTypeFrom(mime),
        ),
      );

    final streamed = await req.send();
    final res = await http.Response.fromStream(streamed);

    if (res.statusCode >= 200 && res.statusCode < 300) {
      final body = jsonDecode(res.body) as Map<String, dynamic>;
      final url = body['secure_url'] as String?;
      if (url == null || url.isEmpty) {
        throw Exception('Upload thành công nhưng không có secure_url trả về.');
      }
      return url;
    } else {
      String reason;
      try {
        final body = jsonDecode(res.body);
        reason = body['error']?['message']?.toString() ?? res.body;
      } catch (_) {
        reason = res.body;
      }
      throw Exception('Cloudinary upload failed (${res.statusCode}): $reason');
    }
  }

  MediaType _mediaTypeFrom(String mime) {
    final parts = mime.split('/');
    if (parts.length == 2) return MediaType(parts[0], parts[1]);
    // ❗ Bỏ const ở đây để tránh lỗi "const_with_non_const"
    return MediaType('image', 'jpeg');
  }
}
