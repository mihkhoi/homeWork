import 'package:flutter/material.dart';
import '../models/cart_item.dart';

class CartItemWidget extends StatelessWidget {
  final CartItem item;
  final VoidCallback onIncrease;
  final VoidCallback onDecrease;
  final VoidCallback onRemove;

  const CartItemWidget({
    super.key,
    required this.item,
    required this.onIncrease,
    required this.onDecrease,
    required this.onRemove,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Image.network(
        item.product.imageUrl,
        width: 50,
        height: 50,
        fit: BoxFit.cover,
        errorBuilder: (context, error, stackTrace) =>
            const Icon(Icons.broken_image),
      ),
      title: Text(item.product.title),
      subtitle: Text(
        'Giá: \$${item.product.price.toStringAsFixed(2)} x ${item.quantity}',
      ),
      trailing: SizedBox(
        width: 130,
        child: Row(
          children: [
            IconButton(icon: const Icon(Icons.remove), onPressed: onDecrease),
            Text('${item.quantity}'),
            IconButton(icon: const Icon(Icons.add), onPressed: onIncrease),
            IconButton(icon: const Icon(Icons.delete), onPressed: onRemove),
          ],
        ),
      ),
    );
  }
}
