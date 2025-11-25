import 'package:flutter/material.dart';

import '../services/firestore_data_service.dart';
import 'student_list_screen.dart';

class TeacherGradeManagementScreen extends StatefulWidget {
  const TeacherGradeManagementScreen({super.key});

  @override
  State<TeacherGradeManagementScreen> createState() =>
      _TeacherGradeManagementScreenState();
}

class _TeacherGradeManagementScreenState
    extends State<TeacherGradeManagementScreen> {
  final _formKey = GlobalKey<FormState>();
  final _studentIdController = TextEditingController();
  final _subjectController = TextEditingController();
  final _midtermController = TextEditingController();
  final _finalController = TextEditingController();

  final _dataService = FirestoreDataService();
  bool _isSaving = false;

  Future<void> _saveGrade() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSaving = true);
    try {
      await _dataService.upsertGrade(
        studentId: _studentIdController.text.trim(),
        subject: _subjectController.text.trim(),
        midterm: double.parse(_midtermController.text.trim()),
        finalScore: double.parse(_finalController.text.trim()),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Lưu điểm thành công')));
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Lỗi lưu điểm: $e')));
    } finally {
      if (mounted) setState(() => _isSaving = false);
    }
  }

  @override
  void dispose() {
    _studentIdController.dispose();
    _subjectController.dispose();
    _midtermController.dispose();
    _finalController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Quản lý điểm (Giáo viên)'),
        actions: [
          IconButton(
            icon: const Icon(Icons.list_alt),
            tooltip: 'Danh sách học sinh',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const StudentListScreen()),
              );
            },
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              // Gợi ý: giáo viên bấm icon trên AppBar để xem / copy studentId
              Text(
                'Nhập studentId (UID) của học sinh.\n'
                'Có thể bấm icon danh sách trên thanh AppBar để xem & copy UID.',
                style: Theme.of(context).textTheme.bodySmall,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _studentIdController,
                decoration: const InputDecoration(
                  labelText: 'studentId (UID học sinh)',
                ),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập studentId' : null,
              ),
              TextFormField(
                controller: _subjectController,
                decoration: const InputDecoration(
                  labelText: 'Môn học (VD: Toán)',
                ),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập môn học' : null,
              ),
              TextFormField(
                controller: _midtermController,
                decoration: const InputDecoration(labelText: 'Điểm giữa kỳ'),
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập điểm giữa kỳ' : null,
              ),
              TextFormField(
                controller: _finalController,
                decoration: const InputDecoration(labelText: 'Điểm cuối kỳ'),
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập điểm cuối kỳ' : null,
              ),
              const SizedBox(height: 16),
              _isSaving
                  ? const Center(child: CircularProgressIndicator())
                  : ElevatedButton(
                      onPressed: _saveGrade,
                      child: const Text('Lưu điểm'),
                    ),
            ],
          ),
        ),
      ),
    );
  }
}
