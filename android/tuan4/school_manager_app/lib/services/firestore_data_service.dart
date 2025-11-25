import 'package:cloud_firestore/cloud_firestore.dart';

import '../models/schedule.dart';
import '../models/grade.dart';

class FirestoreDataService {
  final FirebaseFirestore _db = FirebaseFirestore.instance;

  // ========== LỊCH HỌC / LỊCH DẠY ==========

  /// Thêm một lịch dạy / lịch học vào collection "schedules"
  Future<void> addSchedule({
    required String day,
    required String subject,
    required String time,
    required String room,
    required String teacherId,
    String? classId,
    String? studentId,
  }) async {
    await _db.collection('schedules').add({
      'day': day,
      'subject': subject,
      'time': time,
      'room': room,
      'teacherId': teacherId,
      'classId': classId,
      'studentId': studentId,
      'createdAt': FieldValue.serverTimestamp(),
    });
  }

  /// Lịch cho học sinh: theo studentId hoặc classId
  Stream<List<Schedule>> watchSchedulesForStudent({
    required String studentId,
    String? classId,
  }) {
    Query query = _db.collection('schedules');

    if (classId != null && classId.isNotEmpty) {
      query = query.where('classId', isEqualTo: classId);
    } else {
      query = query.where('studentId', isEqualTo: studentId);
    }

    return query.snapshots().map(
      (snapshot) => snapshot.docs.map(Schedule.fromDoc).toList(),
    );
  }

  /// Lịch dạy cho giáo viên: filter theo teacherId
  Stream<List<Schedule>> watchSchedulesForTeacher(String teacherId) {
    final query = _db
        .collection('schedules')
        .where('teacherId', isEqualTo: teacherId);

    return query.snapshots().map(
      (snapshot) => snapshot.docs.map(Schedule.fromDoc).toList(),
    );
  }

  // ========== ĐIỂM SỐ ==========

  /// Lấy stream điểm của một học sinh
  Stream<List<Grade>> watchGradesForStudent(String studentId) {
    final query = _db
        .collection('grades')
        .where('studentId', isEqualTo: studentId);

    return query.snapshots().map(
      (snapshot) => snapshot.docs.map(Grade.fromDoc).toList(),
    );
  }

  /// Giáo viên thêm / cập nhật điểm cho một học sinh
  Future<void> upsertGrade({
    required String studentId,
    required String subject,
    required double midterm,
    required double finalScore,
  }) async {
    final query = await _db
        .collection('grades')
        .where('studentId', isEqualTo: studentId)
        .where('subject', isEqualTo: subject)
        .limit(1)
        .get();

    if (query.docs.isEmpty) {
      // Thêm mới
      await _db.collection('grades').add({
        'studentId': studentId,
        'subject': subject,
        'midterm': midterm,
        'final': finalScore,
        'createdAt': FieldValue.serverTimestamp(),
      });
    } else {
      // Cập nhật
      await query.docs.first.reference.update({
        'midterm': midterm,
        'final': finalScore,
        'updatedAt': FieldValue.serverTimestamp(),
      });
    }
  }
}
