import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/app_transaction.dart';

class FirestoreService {
  final _db = FirebaseFirestore.instance;

  CollectionReference<Map<String, dynamic>> _txCol(String uid) {
    return _db.collection('users').doc(uid).collection('transactions');
  }

  Future<void> addTransaction(String uid, AppTransaction tx) async {
    await _txCol(uid).add(tx.toMap());
  }

  Stream<List<AppTransaction>> watchTransactions(String uid) {
    return _txCol(uid)
        .orderBy('date', descending: true)
        .snapshots()
        .map(
          (snapshot) => snapshot.docs
              .map((doc) => AppTransaction.fromDoc(doc.id, doc.data()))
              .toList(),
        );
  }
}
