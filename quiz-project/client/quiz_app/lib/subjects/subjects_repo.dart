import '../core/api.dart';

class Subject {
  final int id; final String code; final String name;
  Subject({required this.id, required this.code, required this.name});
  factory Subject.fromJson(Map<String,dynamic> j)=>Subject(id:j['id'], code:j['code'], name:j['name']);
}

class SubjectsRepo {
  Future<List<Subject>> list() async {
    final r = await Api.dio.get('/subjects');
    return (r.data as List).map((e)=>Subject.fromJson(e)).toList();
  }
}
