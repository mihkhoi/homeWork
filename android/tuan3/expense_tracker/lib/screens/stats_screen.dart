import 'dart:math';
import 'package:flutter/material.dart';
import '../models/app_transaction.dart';

class StatsScreen extends StatelessWidget {
  final List<AppTransaction> transactions;
  const StatsScreen({super.key, required this.transactions});

  Map<String, double> _groupByCategory() {
    final map = <String, double>{};
    for (var tx in transactions) {
      map[tx.category] = (map[tx.category] ?? 0) + tx.amount;
    }
    return map;
  }

  @override
  Widget build(BuildContext context) {
    final data = _groupByCategory();
    return Scaffold(
      appBar: AppBar(title: const Text('Phân tích chi tiêu')),
      body: data.isEmpty
          ? const Center(child: Text('Chưa có dữ liệu'))
          : Column(
              children: [
                const SizedBox(height: 16),
                SizedBox(
                  height: 250,
                  child: CustomPaint(
                    painter: _PieChartPainter(data),
                    child: Center(
                      child: Text(
                        'Tổng: ${data.values.fold<double>(0, (p, e) => p + e).toStringAsFixed(0)} đ',
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                Expanded(
                  child: ListView(
                    children: data.entries
                        .map(
                          (e) => ListTile(
                            leading: const Icon(Icons.label),
                            title: Text(e.key),
                            trailing: Text(e.value.toStringAsFixed(0)),
                          ),
                        )
                        .toList(),
                  ),
                ),
              ],
            ),
    );
  }
}

class _PieChartPainter extends CustomPainter {
  final Map<String, double> data;
  _PieChartPainter(this.data);

  @override
  void paint(Canvas canvas, Size size) {
    final total = data.values.fold<double>(
      0,
      (previousValue, element) => previousValue + element,
    );
    if (total == 0) return;

    final center = Offset(size.width / 2, size.height / 2);
    final radius = min(size.width, size.height) / 2.5;
    final rect = Rect.fromCircle(center: center, radius: radius);
    final paint = Paint()
      ..style = PaintingStyle.stroke
      ..strokeWidth = radius;

    double startAngle = -pi / 2;
    int i = 0;
    for (final entry in data.entries) {
      final sweep = (entry.value / total) * 2 * pi;
      // Tạm dùng HSLColor để đổi màu cho từng mảnh
      final color = HSLColor.fromAHSL(1, (i * 60) % 360.0, 0.6, 0.6).toColor();
      paint.color = color;
      canvas.drawArc(rect, startAngle, sweep, false, paint);
      startAngle += sweep;
      i++;
    }
  }

  @override
  bool shouldRepaint(covariant _PieChartPainter oldDelegate) =>
      oldDelegate.data != data;
}
