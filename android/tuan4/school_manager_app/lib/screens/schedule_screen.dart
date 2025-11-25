import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../models/schedule.dart';
import '../providers/auth_provider.dart';
import '../services/firestore_data_service.dart';

class ScheduleScreen extends StatelessWidget {
  const ScheduleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final user = auth.user;
    final profile = auth.userProfile ?? {};
    final role = profile['role'] as String? ?? 'student';

    if (user == null) {
      return const Center(child: Text('Chưa đăng nhập'));
    }

    final dataService = FirestoreDataService();

    Stream<List<Schedule>> stream;

    if (role == 'teacher') {
      // Lịch dạy giáo viên
      stream = dataService.watchSchedulesForTeacher(user.uid);
    } else {
      // Học sinh / phụ huynh
      final classId = profile['classId'] as String?;
      // phụ huynh có thể có childId -> dùng childId làm studentId
      final String studentId = (role == 'parent' && profile['childId'] != null)
          ? profile['childId'] as String
          : user.uid;

      stream = dataService.watchSchedulesForStudent(
        studentId: studentId,
        classId: classId,
      );
    }

    return StreamBuilder<List<Schedule>>(
      stream: stream,
      builder: (context, snapshot) {
        if (snapshot.hasError) {
          return Center(child: Text('Lỗi tải lịch học: ${snapshot.error}'));
        }

        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        }

        final schedules = snapshot.data ?? [];

        if (schedules.isEmpty) {
          return const Center(child: Text('Chưa có lịch học'));
        }

        return ListView.builder(
          itemCount: schedules.length,
          itemBuilder: (context, index) {
            final item = schedules[index];
            return Card(
              margin: const EdgeInsets.all(8),
              child: ListTile(
                title: Text('${item.day} - ${item.subject}'),
                subtitle: Text('Tiết: ${item.time} - Phòng: ${item.room}'),
              ),
            );
          },
        );
      },
    );
  }
}
