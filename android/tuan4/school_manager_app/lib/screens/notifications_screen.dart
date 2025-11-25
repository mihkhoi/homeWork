import 'package:flutter/material.dart';

class NotificationsScreen extends StatelessWidget {
  const NotificationsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final notifications = [
      'Thông báo nghỉ học ngày 20/10',
      'Lịch thi cuối kỳ',
      'Họp phụ huynh tuần sau',
    ];

    return ListView.builder(
      itemCount: notifications.length,
      itemBuilder: (_, index) {
        return Card(
          margin: const EdgeInsets.all(8),
          child: ListTile(
            leading: const Icon(Icons.notifications),
            title: Text(notifications[index]),
          ),
        );
      },
    );
  }
}
