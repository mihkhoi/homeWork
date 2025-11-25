import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';

import '../config/cloudinary_config.dart';

class CloudinaryService {
  Future<String?> uploadImage(XFile file) async {
    final url = Uri.parse(
      'https://api.cloudinary.com/v1_1/$cloudinaryCloudName/image/upload',
    );

    final bytes = await file.readAsBytes();

    final request = http.MultipartRequest('POST', url)
      ..fields['upload_preset'] = cloudinaryUploadPreset
      ..files.add(
        http.MultipartFile.fromBytes('file', bytes, filename: file.name),
      );

    final response = await request.send();
    final resBody = await http.Response.fromStream(response);

    if (resBody.statusCode == 200) {
      final data = jsonDecode(resBody.body);
      return data['secure_url'] as String;
    } else {
      throw Exception('Upload failed: ${resBody.body}');
    }
  }
}
