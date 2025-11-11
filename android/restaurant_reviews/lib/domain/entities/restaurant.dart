class Restaurant {
  final String id;
  final String name;
  final String address;
  final String photoUrl;
  final double ratingAvg;
  final int ratingCount;

  const Restaurant({
    required this.id,
    required this.name,
    required this.address,
    required this.photoUrl,
    required this.ratingAvg,
    required this.ratingCount,
  });

  factory Restaurant.fromMap(String id, Map<String, dynamic> m) => Restaurant(
    id: id,
    name: m['name'] ?? '',
    address: m['address'] ?? '',
    photoUrl: m['photoUrl'] ?? '',
    ratingAvg: (m['ratingAvg'] ?? 0).toDouble(),
    ratingCount: (m['ratingCount'] ?? 0) as int,
  );

  Map<String, dynamic> toMap() => {
    'name': name,
    'address': address,
    'photoUrl': photoUrl,
    'ratingAvg': ratingAvg,
    'ratingCount': ratingCount,
  };
}
