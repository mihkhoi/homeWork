import 'package:flutter/material.dart';
import '../models/app_transaction.dart';
import '../services/firestore_service.dart';

class AddTransactionScreen extends StatefulWidget {
  final String uid;
  const AddTransactionScreen({super.key, required this.uid});

  @override
  State<AddTransactionScreen> createState() => _AddTransactionScreenState();
}

class _AddTransactionScreenState extends State<AddTransactionScreen> {
  final _formKey = GlobalKey<FormState>();
  final _amountCtrl = TextEditingController();
  final _descCtrl = TextEditingController();
  final _cateCtrl = TextEditingController();
  DateTime _date = DateTime.now();
  bool _loading = false;

  final _fs = FirestoreService();

  @override
  void dispose() {
    _amountCtrl.dispose();
    _descCtrl.dispose();
    _cateCtrl.dispose();
    super.dispose();
  }

  Future<void> _pickDate() async {
    final picked = await showDatePicker(
      context: context,
      initialDate: _date,
      firstDate: DateTime(2020),
      lastDate: DateTime(2100),
    );
    if (picked != null) {
      setState(() => _date = picked);
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() {
      _loading = true;
    });

    try {
      // Làm sạch chuỗi số tiền: bỏ dấu . ngăn cách nghìn, đổi , thành .
      final raw = _amountCtrl.text.trim();
      final cleaned = raw.replaceAll('.', '').replaceAll(',', '.');

      final amount = double.tryParse(cleaned);
      if (amount == null) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(const SnackBar(content: Text('Số tiền không hợp lệ')));
        return;
      }

      final tx = AppTransaction(
        id: '',
        amount: amount,
        description: _descCtrl.text,
        category: _cateCtrl.text,
        date: _date,
      );

      await _fs.addTransaction(widget.uid, tx);

      if (mounted) {
        Navigator.pop(context); // quay lại màn hình trước
      }
    } catch (e) {
      // Lỗi mạng / Firestore / parse...
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('Lỗi khi lưu: $e')));
      }
    } finally {
      if (mounted) {
        setState(() {
          _loading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thêm giao dịch')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _amountCtrl,
                decoration: const InputDecoration(labelText: 'Số tiền'),
                keyboardType: TextInputType.number,
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập số tiền' : null,
              ),
              TextFormField(
                controller: _cateCtrl,
                decoration: const InputDecoration(
                  labelText: 'Danh mục (ăn uống, đi lại…)',
                ),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Nhập danh mục' : null,
              ),
              TextFormField(
                controller: _descCtrl,
                decoration: const InputDecoration(labelText: 'Mô tả'),
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Text('Ngày: ${_date.day}/${_date.month}/${_date.year}'),
                  const Spacer(),
                  TextButton(
                    onPressed: _pickDate,
                    child: const Text('Chọn ngày'),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              _loading
                  ? const Center(child: CircularProgressIndicator())
                  : ElevatedButton(
                      onPressed: _submit,
                      child: const Text('Lưu'),
                    ),
            ],
          ),
        ),
      ),
    );
  }
}
