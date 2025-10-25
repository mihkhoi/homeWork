import '../core/api.dart';

class ExamRepo {
  Future<int> createExam({required int subjectId, required String title, int total=1, int duration=10}) async {
    final r = await Api.dio.post('/exams', data: {
      'subjectId': subjectId, 'title': title, 'totalQuestions': total, 'durationMinutes': duration
    });
    return r.data['id'] as int;
  }

  Future<void> generate(int examId) async {
    await Api.dio.post('/exams/$examId/generate');
  }

  Future<Map<String, dynamic>> getExam(int id) async {
    final r = await Api.dio.get('/exams/$id');
    return r.data as Map<String,dynamic>;
  }
}

class AttemptRepo {
  Future<int> start(int examId) async {
    final r = await Api.dio.post('/attempts/start', queryParameters: {'examId': examId});
    return r.data['attemptId'];
  }
  Future<void> answer(int attemptId, int questionId, {int? choiceId, String? fillText}) async {
    await Api.dio.post('/attempts/$attemptId/answer', data: {
      'questionId': questionId, 'choiceId': choiceId, 'fillText': fillText
    });
  }
  Future<double> submit(int attemptId) async {
    final r = await Api.dio.post('/attempts/$attemptId/submit');
    return (r.data['score'] as num).toDouble();
  }
}
