import '../models/book.dart';

/// Service giả: thay vì gọi HTTP, trả về danh sách sách local.
/// Khi bạn có API thật thì sửa lại hàm fetchBooks().
class BookApiService {
  // Không cần baseUrl nữa, nhưng giữ constructor trống cho dễ dùng
  BookApiService();

  Future<List<Book>> fetchBooks() async {
    // Giả lập độ trễ gọi mạng
    await Future.delayed(const Duration(milliseconds: 500));

    return [
      Book(
        id: 1,
        title: 'Ứng dụng đọc sách',
        author: 'Minh',
        description:
            'Một câu chuyện về lập trình viên xây dựng ứng dụng đọc sách điện tử.',
        coverUrl: 'assets/images/book1_cover.jpg',
        assetPath: 'assets/books/book1.txt',
      ),
      Book(
        id: 2,
        title: 'Thư viện số',
        author: 'Lan',
        description:
            'Câu chuyện về thư viện truyền thống kết hợp với công nghệ sách điện tử.',
        coverUrl: 'assets/images/book2_cover.jpg',
        assetPath: 'assets/books/book2.txt',
      ),
    ];
  }
}
