import 'dart:convert';
import 'dart:typed_data';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class ApiService {
  static String baseUrl = 'http://127.0.0.1:8000';
  static String get base => baseUrl;

  static Future<void> init() async {
    final sp = await SharedPreferences.getInstance();
    final cached = sp.getString('api_base');
    if (cached != null && await _health(cached)) {
      baseUrl = cached;
      return;
    }
    final quick = [
      'http://10.0.2.2:8000',
      'http://127.0.0.1:8000',
      'http://localhost:8000',
    ];
    for (final b in quick) {
      if (await _health(b)) {
        baseUrl = b;
        await sp.setString('api_base', b);
        return;
      }
    }
    final prefixes = await _prefixes();
    for (final p in prefixes) {
      final found = await _scanPrefix(p);
      if (found != null) {
        baseUrl = found;
        await sp.setString('api_base', found);
        return;
      }
    }
  }

  static Future<bool> _health(String base) async {
    try {
      final r = await http
          .get(Uri.parse('$base/health'))
          .timeout(const Duration(milliseconds: 800));
      if (r.statusCode != 200) return false;
      final m = jsonDecode(r.body);
      return m is Map<String, dynamic> && (m['ok'] == true);
    } catch (_) {
      return false;
    }
  }

  static Future<List<String>> _prefixes() async {
    final out = <String>{};
    try {
      final nets = await NetworkInterface.list(
          type: InternetAddressType.IPv4, includeLoopback: false);
      for (final n in nets) {
        for (final a in n.addresses) {
          final ip = a.address;
          final parts = ip.split('.');
          if (parts.length == 4) {
            final p0 = parts[0];
            final p1 = parts[1];
            final p2 = parts[2];
            if (p0 == '10' || p0 == '192' || (p0 == '172')) {
              out.add('$p0.$p1.$p2.');
            }
          }
        }
      }
    } catch (_) {}
    return out.toList();
  }

  static Future<String?> _scanPrefix(String prefix) async {
    const batch = 16;
    final hosts = List<int>.generate(254, (i) => i + 1);
    for (int i = 0; i < hosts.length; i += batch) {
      final chunk = hosts.sublist(
          i, (i + batch > hosts.length) ? hosts.length : i + batch);
      final futs = chunk.map((h) async {
        final base = 'http://${prefix}$h:8000';
        final ok = await _health(base);
        return ok ? base : null;
      }).toList();
      final res = await Future.wait(futs, eagerError: false);
      for (final r in res) {
        if (r != null) return r;
      }
    }
    return null;
  }

  static Uri _u(String path, [Map<String, String>? query]) {
    final uri = Uri.parse('$baseUrl$path');
    return query == null ? uri : uri.replace(queryParameters: query);
  }

  // ---------------- PESTS ----------------
  static Future<List<Map<String, dynamic>>> getPests({String? q}) async {
    final uri = _u('/pests', (q != null && q.trim().isNotEmpty) ? {'q': q} : null);
    final r = await http.get(uri).timeout(const Duration(seconds: 12));
    if (r.statusCode != 200) {
      throw Exception('GET /pests failed ${r.statusCode}');
    }
    final data = jsonDecode(r.body) as Map<String, dynamic>;
    final items = (data['items'] as List?) ?? const [];
    return items
        .map<Map<String, dynamic>>((e) => Map<String, dynamic>.from(e as Map))
        .toList();
  }

  static Future<Map<String, dynamic>?> getPest(String code) async {
    final r = await http
        .get(_u('/pests/$code'))
        .timeout(const Duration(seconds: 12));
    if (r.statusCode != 200) {
      throw Exception('GET /pests/$code failed ${r.statusCode}');
    }
    final data = jsonDecode(r.body) as Map<String, dynamic>;
    final p = data['pest'];
    return p == null ? null : Map<String, dynamic>.from(p as Map);
  }

  // ---------------- DRUGS ----------------
  static Future<List<Map<String, dynamic>>> getDrugs() async {
    final r = await http.get(_u('/drugs')).timeout(const Duration(seconds: 12));
    if (r.statusCode != 200) {
      throw Exception('GET /drugs failed ${r.statusCode}');
    }
    final data = jsonDecode(r.body) as Map<String, dynamic>;
    final items = (data['items'] as List?) ?? const [];
    return items
        .map<Map<String, dynamic>>((e) => Map<String, dynamic>.from(e as Map))
        .toList();
  }

  static Future<List<Map<String, dynamic>>> getDrugsForPest(String code) async {
    final r = await http
        .get(_u('/pests/$code/drugs'))
        .timeout(const Duration(seconds: 12));
    if (r.statusCode != 200) {
      throw Exception('GET /pests/$code/drugs failed ${r.statusCode}');
    }
    final data = jsonDecode(r.body) as Map<String, dynamic>;
    final items = (data['items'] as List?) ?? const [];
    return items
        .map<Map<String, dynamic>>((e) => Map<String, dynamic>.from(e as Map))
        .toList();
  }

  // ---------------- CLASSIFY (top-k) ----------------
  /// Trả list:
  /// [{"prediction":{"code":"..","prob":..},"detail":{...},"drugs":[...]}]
  static Future<List<Map<String, dynamic>>> classify(Uint8List bytes) async {
    final req = http.MultipartRequest('POST', _u('/classify'))
      ..files.add(http.MultipartFile.fromBytes('file', bytes, filename: 'photo.jpg'));
    final streamed = await req.send().timeout(const Duration(seconds: 25));
    final res = await http.Response.fromStream(streamed);
    if (res.statusCode != 200) {
      throw Exception('POST /classify failed ${res.statusCode}: ${res.body}');
    }
    final data = jsonDecode(res.body) as Map<String, dynamic>;
    final results = (data['results'] as List?) ?? const [];
    return results
        .map<Map<String, dynamic>>((e) => Map<String, dynamic>.from(e as Map))
        .toList();
  }

  // ---------------- AUTH ----------------
  static Future<bool> register(String username, String password) async {
    final r = await http
        .post(
          _u('/auth/register'),
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          body: {'username': username, 'password': password},
        )
        .timeout(const Duration(seconds: 12));
    if (r.statusCode != 200) return false;
    final ok = (jsonDecode(r.body) as Map<String, dynamic>)['ok'] == true;
    return ok;
  }

  static Future<bool> login(String username, String password) async {
    final r = await http
        .post(
          _u('/auth/login'),
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          body: {'username': username, 'password': password},
        )
        .timeout(const Duration(seconds: 12));
    if (r.statusCode != 200) return false;
    final ok = (jsonDecode(r.body) as Map<String, dynamic>)['ok'] == true;
    return ok;
  }

  // ---------------- HEALTH ----------------
  static Future<bool> health() async {
    try {
      final r = await http.get(_u('/health')).timeout(const Duration(seconds: 6));
      if (r.statusCode != 200) return false;
      final data = jsonDecode(r.body) as Map<String, dynamic>;
      return data['ok'] == true;
    } catch (_) {
      return false;
    }
  }
}
