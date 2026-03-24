package com.example.shop;

import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.entity.Voucher;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.repository.VoucherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Component public class SeedData implements CommandLineRunner {

  private
    final ProductRepository productRepo;
  private
    final UserRepository userRepo;
  private
    final VoucherRepository voucherRepo;

  public
    SeedData(ProductRepository productRepo, UserRepository userRepo, VoucherRepository voucherRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.voucherRepo = voucherRepo;
    }

    @Override public void run(String... args) {
        seedAdmin();
        seedProducts();
        seedVouchers();
    }

  private
    void seedAdmin() {
        if (!userRepo.existsByEmail("admin@shop.com")) {
            User admin = new User();
            admin.setEmail("admin@shop.com");
            admin.setPasswordHash(sha256("admin123"));
            admin.setRole("ADMIN");
            admin.setFullName("System Admin");
            admin.setPhone("0900000000");
            admin.setAddress("Admin Office");
            admin.setStatus("ACTIVE");
            userRepo.save(admin);
        }
    }

  private
    void seedProducts() {
        if (productRepo.count() == 0) {
            productRepo.save(make("Áo thun basic", "Áo thun cotton mặc hàng ngày, dễ phối đồ.", "Fashion", "", 99000, 50, true));
            productRepo.save(make("Quần jean slim fit", "Quần jean form slim, phù hợp nhiều phong cách.", "Fashion", "", 299000, 20, true));
            productRepo.save(make("Tai nghe Bluetooth", "Tai nghe không dây pin lâu, âm thanh ổn định.", "Electronics", "", 499000, 15, true));
            productRepo.save(make("Chuột gaming RGB", "Chuột gaming có LED RGB và DPI cao.", "Electronics", "", 259000, 18, true));
            productRepo.save(make("Bình giữ nhiệt", "Giữ nóng lạnh nhiều giờ.", "Home", "", 149000, 35, true));
            productRepo.save(make("Giày sneaker trắng", "Sneaker basic phù hợp đi học, đi chơi.", "Shoes", "", 459000, 12, true));
        }
    }

  private
    void seedVouchers() {
        if (voucherRepo.findByCode("SALE10").isEmpty()) {
            Voucher v = new Voucher();
            v.setCode("SALE10");
            v.setDiscountType("PERCENT");
            v.setDiscountValue(10);
            v.setMinOrderValue(100000);
            v.setMaxDiscount(50000);
            v.setUsageLimit(100);
            v.setUsedCount(0);
            v.setActive(true);
            v.setStartAt(LocalDateTime.now().minusDays(30));
            v.setEndAt(LocalDateTime.now().plusDays(30));
            voucherRepo.save(v);
        }

        if (voucherRepo.findByCode("FIX50000").isEmpty()) {
            Voucher v = new Voucher();
            v.setCode("FIX50000");
            v.setDiscountType("FIXED");
            v.setDiscountValue(50000);
            v.setMinOrderValue(300000);
            v.setMaxDiscount(50000);
            v.setUsageLimit(50);
            v.setUsedCount(0);
            v.setActive(true);
            v.setStartAt(LocalDateTime.now().minusDays(30));
            v.setEndAt(LocalDateTime.now().plusDays(30));
            voucherRepo.save(v);
        }
    }

  private
    Product make(String name, String description, String category, String imageUrl, long price, int stock, boolean active) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setCategory(category);
        p.setImageUrl(imageUrl);
        p.setPrice(price);
        p.setStock(stock);
        p.setActive(active);
        return p;
    }

  private
    String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}
