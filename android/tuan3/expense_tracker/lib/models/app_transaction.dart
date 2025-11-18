class AppTransaction {
  final String id;
  final double amount;
  final String description;
  final String category;
  final DateTime date;

  AppTransaction({
    required this.id,
    required this.amount,
    required this.description,
    required this.category,
    required this.date,
  });

  Map<String, dynamic> toMap() {
    return {
      'amount': amount,
      'description': description,
      'category': category,
      'date': date.toIso8601String(),
    };
  }

  factory AppTransaction.fromDoc(String id, Map<String, dynamic> map) {
    return AppTransaction(
      id: id,
      amount: (map['amount'] as num).toDouble(),
      description: map['description'] ?? '',
      category: map['category'] ?? '',
      date: DateTime.parse(map['date'] as String),
    );
  }
}
