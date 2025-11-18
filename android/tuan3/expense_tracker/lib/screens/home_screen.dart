import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';

import '../models/app_transaction.dart';
import '../services/auth_service.dart';
import '../services/firestore_service.dart';
import '../widgets/transaction_tile.dart';
import 'add_transaction_screen.dart';
import 'stats_screen.dart';

class HomeScreen extends StatelessWidget {
  final User user;
  HomeScreen({super.key, required this.user});

  final _fs = FirestoreService();
  final _auth = AuthService();

  Stream<List<AppTransaction>> _streamTx() => _fs.watchTransactions(user.uid);

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<List<AppTransaction>>(
      stream: _streamTx(),
      builder: (context, snapshot) {
        final items = snapshot.data ?? [];

        final totalToday = items
            .where(
              (tx) =>
                  tx.date.year == DateTime.now().year &&
                  tx.date.month == DateTime.now().month &&
                  tx.date.day == DateTime.now().day,
            )
            .fold<double>(0, (p, e) => p + e.amount);

        return Scaffold(
          body: CustomScrollView(
            slivers: [
              SliverAppBar(
                pinned: true,
                expandedHeight: 140,
                flexibleSpace: FlexibleSpaceBar(
                  title: Text('Xin chào, ${user.email}'),
                  background: Padding(
                    padding: const EdgeInsets.only(left: 16, bottom: 32),
                    child: Align(
                      alignment: Alignment.bottomLeft,
                      child: Text(
                        'Hôm nay đã chi: ${totalToday.toStringAsFixed(0)} đ',
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 18,
                        ),
                      ),
                    ),
                  ),
                ),
                actions: [
                  IconButton(
                    icon: const Icon(Icons.pie_chart),
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => StatsScreen(transactions: items),
                        ),
                      );
                    },
                  ),
                  IconButton(
                    icon: const Icon(Icons.logout),
                    onPressed: () => _auth.signOut(),
                  ),
                ],
              ),
              if (snapshot.connectionState == ConnectionState.waiting)
                const SliverFillRemaining(
                  child: Center(child: CircularProgressIndicator()),
                )
              else if (items.isEmpty)
                const SliverFillRemaining(
                  child: Center(child: Text('Chưa có giao dịch nào')),
                )
              else
                SliverList(
                  delegate: SliverChildBuilderDelegate(
                    (context, index) => TransactionTile(tx: items[index]),
                    childCount: items.length,
                  ),
                ),
            ],
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => AddTransactionScreen(uid: user.uid),
                ),
              );
            },
            child: const Icon(Icons.add),
          ),
        );
      },
    );
  }
}
