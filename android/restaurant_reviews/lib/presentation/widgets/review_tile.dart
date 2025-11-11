import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import '../../../domain/entities/review.dart';

class ReviewTile extends StatelessWidget {
  final Review r;
  const ReviewTile({super.key, required this.r});
  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: CircleAvatar(
        backgroundImage: r.userAvatar == null
            ? null
            : NetworkImage(r.userAvatar!),
      ),
      title: Text('${r.userName}  •  ${'⭐' * r.rating}'),
      subtitle: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(r.content),
          if (r.imageUrl != null && r.imageUrl!.isNotEmpty)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: CachedNetworkImage(
                imageUrl: r.imageUrl!,
                height: 140,
                fit: BoxFit.cover,
              ),
            ),
        ],
      ),
    );
  }
}
