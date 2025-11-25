import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../providers/auth_provider.dart';
import '../services/firestore_data_service.dart';

class AddScheduleScreen extends StatefulWidget {
  const AddScheduleScreen({super.key});

  @override
  State<AddScheduleScreen> createState() => _AddScheduleScreenState();
}

class _AddScheduleScreenState extends State<AddScheduleScreen> {
  final _formKey = GlobalKey<FormState>();
  final _dayController = TextEditingController();
  final _subjectController = TextEditingController();
  final _timeController = TextEditingController();
  final _roomController = TextEditingController();
  final _classIdController = TextEditingController();

  bool _isSaving = false;
  final _dataService = FirestoreDataService();

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) return;

    final auth = context.read<AuthProvider>();
    final user = auth.user;
    if (user == null) return;

    setState(() => _isSaving = true);
    try {
      await _dataService.addSchedule(
        day: _dayController.text.trim(),
        subject: _subjectController.text.trim(),
        time: _timeController.text.trim(),
        room: _roomController.text.trim(),
        teacherId: user.uid, // lịch của GV đang login
        classId: _classIdController.text.trim().isEmpty
            ? null
            : _classIdController.text.trim(),
      );

      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Thêm lịch dạy thành công')));
      Navigator.pop(context); // quay lại
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Lỗi: $e')));
    } finally {
      if (mounted) setState(() => _isSaving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thêm lịch dạy')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _dayController,
                decoration: const InputDecoration(labelText: 'Thứ (VD: Thứ 2)'),
                validator: (v) => v == null || v.isEmpty ? 'Nhập thứ' : null,
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
                controller: _timeController,
                decoration: const InputDecoration(
                  labelText: 'Thời gian (VD: 7:00 - 9:00)',
                ),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập thời gian' : null,
              ),
              TextFormField(
                controller: _roomController,
                decoration: const InputDecoration(labelText: 'Phòng (VD: 101)'),
                validator: (v) => v == null || v.isEmpty ? 'Nhập phòng' : null,
              ),
              TextFormField(
                controller: _classIdController,
                decoration: const InputDecoration(
                  labelText: 'Lớp (VD: 10A1) - có thể bỏ trống',
                ),
              ),
              const SizedBox(height: 16),
              _isSaving
                  ? const Center(child: CircularProgressIndicator())
                  : ElevatedButton(
                      onPressed: _save,
                      child: const Text('Lưu lịch dạy'),
                    ),
            ],
          ),
        ),
      ),
    );
  }
}
