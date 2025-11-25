import 'package:cloud_firestore/cloud_firestore.dart';

class Grade {
  final String id;
  final String studentId;
  final String subject;
  final double midterm;
  final double finalScore;

  Grade({
    required this.id,
    required this.studentId,
    required this.subject,
    required this.midterm,
    required this.finalScore,
  });

  factory Grade.fromDoc(DocumentSnapshot doc) {
    final data = doc.data() as Map<String, dynamic>? ?? {};
    return Grade(
      id: doc.id,
      studentId: data['studentId'] as String? ?? '',
      subject: data['subject'] as String? ?? '',
      midterm: (data['midterm'] as num?)?.toDouble() ?? 0,
      finalScore: (data['final'] as num?)?.toDouble() ?? 0,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'studentId': studentId,
      'subject': subject,
      'midterm': midterm,
      'final': finalScore,
    };
  }
}
