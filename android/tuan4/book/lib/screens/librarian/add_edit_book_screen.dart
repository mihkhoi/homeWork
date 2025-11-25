import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

import '../../models/book.dart';
import '../../providers/book_provider.dart';
import '../../services/cloudinary_uploader.dart';

class AddEditBookScreen extends StatefulWidget {
  final Book? book;
  const AddEditBookScreen({super.key, this.book});

  @override
  State<AddEditBookScreen> createState() => _AddEditBookScreenState();
}

class _AddEditBookScreenState extends State<AddEditBookScreen> {
  final _formKey = GlobalKey<FormState>();
  final _titleCtrl = TextEditingController();
  final _authorCtrl = TextEditingController();
  final _descCtrl = TextEditingController();
  final _categoryCtrl = TextEditingController();
  final _totalCtrl = TextEditingController(text: '1');

  String _coverUrl = '';
  bool _isUploading = false;

  @override
  void initState() {
    super.initState();
    if (widget.book != null) {
      _titleCtrl.text = widget.book!.title;
      _authorCtrl.text = widget.book!.author;
      _descCtrl.text = widget.book!.description;
      _categoryCtrl.text = widget.book!.category;
      _totalCtrl.text = widget.book!.totalCopies.toString();
      _coverUrl = widget.book!.coverUrl;
    }
  }

  @override
  void dispose() {
    _titleCtrl.dispose();
    _authorCtrl.dispose();
    _descCtrl.dispose();
    _categoryCtrl.dispose();
    _totalCtrl.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final file = await picker.pickImage(source: ImageSource.gallery);
    if (file == null) return;
    setState(() {
      _isUploading = true;
    });
    try {
      final url = await CloudinaryUploader.uploadCover(file);
      setState(() {
        _coverUrl = url;
      });
    } finally {
      setState(() {
        _isUploading = false;
      });
    }
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) return;

    final total = int.tryParse(_totalCtrl.text) ?? 1;
    final bookProvider = context.read<BookProvider>();

    if (widget.book == null) {
      // ===== THÊM SÁCH MỚI =====
      final newBook = Book(
        id: '',
        title: _titleCtrl.text,
        author: _authorCtrl.text,
        description: _descCtrl.text,
        category: _categoryCtrl.text,
        coverUrl: _coverUrl,
        isAvailable: total > 0,
        totalCopies: total,
        availableCopies: total, // mới thêm thì còn đủ
      );
      await bookProvider.addBook(newBook);
    } else {
      // ===== SỬA SÁCH =====
      final bookId = widget.book!.id;

      // Đếm số lượt mượn đang mở (status = 'borrowing') của cuốn sách này
      final loansSnap = await FirebaseFirestore.instance
          .collection('loans')
          .where('bookId', isEqualTo: bookId)
          .get();

      final int borrowingCount = loansSnap.docs.where((doc) {
        final Map<String, dynamic> data = doc.data();
        return data['status'] == 'borrowing';
      }).length;

      // available = total - số đang mượn
      int newAvailable = total - borrowingCount;
      if (newAvailable < 0) newAvailable = 0;

      await bookProvider.updateBook(bookId, {
        'title': _titleCtrl.text,
        'author': _authorCtrl.text,
        'description': _descCtrl.text,
        'category': _categoryCtrl.text,
        'coverUrl': _coverUrl,
        'totalCopies': total,
        'availableCopies': newAvailable,
        'isAvailable': newAvailable > 0,
      });
    }

    if (mounted) Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    final isEdit = widget.book != null;
    return Scaffold(
      appBar: AppBar(title: Text(isEdit ? 'Sửa sách' : 'Thêm sách')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                if (_coverUrl.isNotEmpty)
                  Image.network(_coverUrl, height: 150, fit: BoxFit.cover),
                const SizedBox(height: 8),
                ElevatedButton.icon(
                  onPressed: _isUploading ? null : _pickImage,
                  icon: const Icon(Icons.image),
                  label: Text(
                    _isUploading
                        ? 'Đang tải lên Cloudinary...'
                        : 'Chọn ảnh bìa',
                  ),
                ),
                TextFormField(
                  controller: _titleCtrl,
                  decoration: const InputDecoration(labelText: 'Tên sách'),
                  validator: (v) =>
                      v == null || v.isEmpty ? 'Nhập tên sách' : null,
                ),
                TextFormField(
                  controller: _authorCtrl,
                  decoration: const InputDecoration(labelText: 'Tác giả'),
                ),
                TextFormField(
                  controller: _categoryCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Danh mục / Thể loại',
                  ),
                ),
                TextFormField(
                  controller: _descCtrl,
                  decoration: const InputDecoration(labelText: 'Mô tả'),
                  maxLines: 3,
                ),
                TextFormField(
                  controller: _totalCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Số bản (totalCopies)',
                  ),
                  keyboardType: TextInputType.number,
                ),
                const SizedBox(height: 16),
                ElevatedButton(onPressed: _save, child: const Text('Lưu')),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
