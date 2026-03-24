package com.example.shop.service;

import com.example.shop.dto.AdminProductRequest;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service public class AdminProductService {

  private
    final ProductRepository productRepo;

  public
    AdminProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

  public
    Product create(AdminProductRequest req) {
        validate(req);

        Product p = new Product();
        p.setName(req.getName().trim());
        p.setDescription(req.getDescription() == null ? "" : req.getDescription().trim());
        p.setCategory(req.getCategory() == null || req.getCategory().isBlank() ? "General" : req.getCategory().trim());
        p.setImageUrl(req.getImageUrl() == null ? "" : req.getImageUrl().trim());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        p.setActive(req.isActive());

        Product saved = productRepo.save(p);
        if (saved.getId() == null) {
            throw new ContractViolationException("POST: product id must exist");
        }
        return saved;
    }

  public
    Product update(Long id, AdminProductRequest req) {
        if (id == null) {
            throw new ContractViolationException("PRE: product id null");
        }
        validate(req);

        Product p = productRepo.findById(id)
                        .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        p.setName(req.getName().trim());
        p.setDescription(req.getDescription() == null ? "" : req.getDescription().trim());
        p.setCategory(req.getCategory() == null || req.getCategory().isBlank() ? "General" : req.getCategory().trim());
        p.setImageUrl(req.getImageUrl() == null ? "" : req.getImageUrl().trim());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        p.setActive(req.isActive());

        Product saved = productRepo.save(p);

        if (!saved.getName().equals(req.getName().trim())) {
            throw new ContractViolationException("POST: product name not updated");
        }

        return saved;
    }

  public
    void toggleActive(Long id) {
        if (id == null) {
            throw new ContractViolationException("PRE: product id null");
        }

        Product p = productRepo.findById(id)
                        .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        p.setActive(!p.isActive());
        productRepo.save(p);
    }

  public
    void delete (Long id) {
        if (id == null) {
            throw new ContractViolationException("PRE: product id null");
        }

        Product p = productRepo.findById(id)
                        .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        productRepo.delete(p);
    }

  private
    void validate(AdminProductRequest req) {
        if (req == null) {
            throw new ContractViolationException("PRE: request null");
        }
        if (req.getName() == null || req.getName().isBlank()) {
            throw new ContractViolationException("PRE: product name must not be blank");
        }
        if (req.getPrice() < 0) {
            throw new ContractViolationException("PRE: price must be >= 0");
        }
        if (req.getStock() < 0) {
            throw new ContractViolationException("PRE: stock must be >= 0");
        }
    }
}
