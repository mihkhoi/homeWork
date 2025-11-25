import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';

import '../models/grade.dart';

class ProgressChartScreen extends StatelessWidget {
  final List<Grade> grades;

  const ProgressChartScreen({super.key, required this.grades});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Biểu đồ điểm số')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: BarChart(
          BarChartData(
            alignment: BarChartAlignment.spaceAround,
            titlesData: FlTitlesData(
              leftTitles: AxisTitles(sideTitles: SideTitles(showTitles: true)),
              bottomTitles: AxisTitles(
                sideTitles: SideTitles(
                  showTitles: true,
                  getTitlesWidget: (value, meta) {
                    final index = value.toInt();
                    if (index < 0 || index >= grades.length) {
                      return const SizedBox.shrink();
                    }
                    return Text(
                      grades[index].subject,
                      style: const TextStyle(fontSize: 10),
                    );
                  },
                ),
              ),
            ),
            barGroups: List.generate(grades.length, (index) {
              final grade = grades[index];
              final avg = (grade.midterm + grade.finalScore) / 2;
              return BarChartGroupData(
                x: index,
                barRods: [BarChartRodData(toY: avg, width: 16)],
              );
            }),
          ),
        ),
      ),
    );
  }
}
