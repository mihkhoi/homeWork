import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../models/grade.dart';
import '../providers/auth_provider.dart';
import '../services/firestore_data_service.dart';
import 'progress_chart_screen.dart';

class GradeScreen extends StatelessWidget {
  const GradeScreen({super.key});

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

    // học sinh: xem điểm của mình; phụ huynh: điểm của con
    final String studentId = (role == 'parent' && profile['childId'] != null)
        ? profile['childId'] as String
        : user.uid;

    final stream = dataService.watchGradesForStudent(studentId);

    return StreamBuilder<List<Grade>>(
      stream: stream,
      builder: (context, snapshot) {
        if (snapshot.hasError) {
          return Center(child: Text('Lỗi tải điểm: ${snapshot.error}'));
        }

        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        }

        final grades = snapshot.data ?? [];

        if (grades.isEmpty) {
          return const Center(child: Text('Chưa có điểm'));
        }

        return Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: DataTable(
                  columns: const [
                    DataColumn(label: Text('Môn học')),
                    DataColumn(label: Text('Giữa kỳ')),
                    DataColumn(label: Text('Cuối kỳ')),
                    DataColumn(label: Text('Trung bình')),
                  ],
                  rows: grades.map((g) {
                    final avg = (g.midterm + g.finalScore) / 2;
                    return DataRow(
                      cells: [
                        DataCell(Text(g.subject)),
                        DataCell(Text(g.midterm.toString())),
                        DataCell(Text(g.finalScore.toString())),
                        DataCell(Text(avg.toStringAsFixed(1))),
                      ],
                    );
                  }).toList(),
                ),
              ),
            ),
            const SizedBox(height: 8),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => ProgressChartScreen(grades: grades),
                  ),
                );
              },
              child: const Text('Xem biểu đồ tiến độ'),
            ),
            const SizedBox(height: 8),
          ],
        );
      },
    );
  }
}
