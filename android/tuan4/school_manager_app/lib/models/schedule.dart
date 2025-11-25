import 'package:cloud_firestore/cloud_firestore.dart';

class Schedule {
  final String id;
  final String day; // Thứ 2, Thứ 3...
  final String subject; // Toán, Lý...
  final String time; // 7:00 - 9:00
  final String room; // 101

  Schedule({
    required this.id,
    required this.day,
    required this.subject,
    required this.time,
    required this.room,
  });

  factory Schedule.fromDoc(DocumentSnapshot doc) {
    final data = doc.data() as Map<String, dynamic>? ?? {};
    return Schedule(
      id: doc.id,
      day: data['day'] as String? ?? '',
      subject: data['subject'] as String? ?? '',
      time: data['time'] as String? ?? '',
      room: data['room'] as String? ?? '',
    );
  }
}
