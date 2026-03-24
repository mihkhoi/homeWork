package com.example.shop.service;

import com.example.shop.entity.CartItem;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class CartService {

  private
    final CartItemRepository cartRepo;
  private
    final ProductRepository productRepo;
  private
    final UserRepository userRepo;

  public
    CartService(CartItemRepository cartRepo,
                ProductRepository productRepo,
                UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

  public
    void addToCart(Long userId, Long productId, int qty) {
        if (userId == null)
            throw new ContractViolationException("PRE: userId null");
        if (productId == null)
            throw new ContractViolationException("PRE: productId null");
        if (qty < 1)
            throw new ContractViolationException("PRE: qty must be >= 1");
        if (!userRepo.existsById(userId))
            throw new ContractViolationException("PRE: user not found");

        Product p = productRepo.findById(productId)
                        .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        if (!p.isActive())
            throw new ContractViolationException("INV: product inactive");
        if (p.getStock() < 0)
            throw new ContractViolationException("INV: stock must be >= 0");
        if (qty > p.getStock())
            throw new ContractViolationException("PRE: insufficient stock");

        CartItem item = cartRepo.findByUserIdAndProductId(userId, productId).orElse(null);

        if (item == null) {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(qty);
            cartRepo.save(newItem);
        } else {
            int newQty = item.getQuantity() + qty;
            if (newQty > p.getStock()) {
                throw new ContractViolationException("PRE: insufficient stock");
            }
            item.setQuantity(newQty);
            cartRepo.save(item);
        }

        ensureCartInvariant(userId);
    }

  public
    void updateQuantity(Long userId, Long productId, int newQty) {
        if (userId == null)
            throw new ContractViolationException("PRE: userId null");
        if (productId == null)
            throw new ContractViolationException("PRE: productId null");
        if (newQty < 1)
            throw new ContractViolationException("PRE: newQty must be >= 1");
        if (!userRepo.existsById(userId))
            throw new ContractViolationException("PRE: user not found");

        Product p = productRepo.findById(productId)
                        .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        if (newQty > p.getStock()) {
            throw new ContractViolationException("PRE: insufficient stock");
        }

        List<CartItem> items = cartRepo.findByUserId(userId);

        CartItem target = null;
        for (CartItem ci : items) {
            if (ci.getProductId().equals(productId)) {
                target = ci;
                break;
            }
        }

        if (target == null) {
            throw new ContractViolationException("PRE: cart item not found");
        }

        target.setQuantity(newQty);
        cartRepo.save(target);

        if (target.getQuantity() != newQty) {
            throw new ContractViolationException("POST: quantity must be updated");
        }

        ensureCartInvariant(userId);
    }

  public
    void removeItem(Long userId, Long productId) {
        if (userId == null || productId == null) {
            throw new ContractViolationException("PRE: ids null");
        }
        cartRepo.deleteByUserIdAndProductId(userId, productId);
        ensureCartInvariant(userId);
    }

  public
    List<CartItem> getCart(Long userId) {
        if (userId == null)
            throw new ContractViolationException("PRE: userId null");
        return cartRepo.findByUserId(userId);
    }

  private
    void ensureCartInvariant(Long userId) {
        List<CartItem> items = cartRepo.findByUserId(userId);
        long total = 0;

        for (CartItem ci : items) {
            if (ci.getQuantity() < 1) {
                throw new ContractViolationException("INV: quantity must be >= 1");
            }

            Product p = productRepo.findById(ci.getProductId())
                            .orElseThrow(()->new ContractViolationException("INV: product missing"));

            if (!p.isActive()) {
                throw new ContractViolationException("INV: cart contains inactive product");
            }
            if (p.getPrice() < 0) {
                throw new ContractViolationException("INV: product price must be >= 0");
            }
            if (ci.getQuantity() > p.getStock()) {
                throw new ContractViolationException("INV: quantity exceeds stock");
            }

            total += p.getPrice() * ci.getQuantity();
        }

        if (total < 0) {
            throw new ContractViolationException("INV: cart total must be >= 0");
        }
    }
}
