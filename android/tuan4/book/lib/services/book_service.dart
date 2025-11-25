import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/book.dart';

class BookService {
  final CollectionReference booksRef = FirebaseFirestore.instance.collection(
    'books',
  );

  Stream<List<Book>> streamBooks() {
    return booksRef.snapshots().map((snapshot) {
      return snapshot.docs
          .map(
            (doc) => Book.fromDoc(doc.id, doc.data() as Map<String, dynamic>),
          )
          .toList();
    });
  }

  Future<void> addBook(Book book) async {
    await booksRef.add(book.toMap());
  }

  Future<void> updateBook(String id, Map<String, dynamic> data) async {
    await booksRef.doc(id).update(data);
  }

  Future<void> deleteBook(String id) async {
    await booksRef.doc(id).delete();
  }
}
