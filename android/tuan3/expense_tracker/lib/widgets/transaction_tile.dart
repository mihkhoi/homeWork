import 'package:flutter/material.dart';
import '../models/app_transaction.dart';

class TransactionTile extends StatelessWidget {
  final AppTransaction tx;

  const TransactionTile({super.key, required this.tx});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(
        '${tx.category} - ${tx.amount.toStringAsFixed(0)} đ',
        style: const TextStyle(fontWeight: FontWeight.bold),
      ),
      subtitle: Text(tx.description),
      trailing: Text(
        '${tx.date.day}/${tx.date.month}',
        style: const TextStyle(fontSize: 12),
      ),
    );
  }
}
