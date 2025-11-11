import 'package:flutter/material.dart';

class RatingBar extends StatelessWidget {
  final int value;
  final void Function(int)? onChanged;
  final double size;
  const RatingBar({
    super.key,
    required this.value,
    this.onChanged,
    this.size = 20,
  });
  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: List.generate(5, (i) {
        final filled = i < value;
        return IconButton(
          iconSize: size,
          padding: EdgeInsets.zero,
          constraints: const BoxConstraints(),
          onPressed: onChanged == null ? null : () => onChanged!(i + 1),
          icon: Icon(
            filled ? Icons.star : Icons.star_border,
            color: Colors.amber,
          ),
        );
      }),
    );
  }
}
