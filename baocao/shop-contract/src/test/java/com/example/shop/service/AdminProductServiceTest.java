package com.example.shop.service;

import com.example.shop.dto.AdminProductRequest;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminProductServiceTest {

  private
    ProductRepository productRepo;
  private
    AdminProductService adminProductService;

    @BeforeEach void setup() {
        productRepo = mock(ProductRepository.class);
        adminProductService = new AdminProductService(productRepo);
    }

  private
    AdminProductRequest validRequest() {
        AdminProductRequest req = new AdminProductRequest();
        req.setName("Áo thun local brand");
        req.setDescription("Áo đẹp");
        req.setCategory("Fashion");
        req.setImageUrl("");
        req.setPrice(199000);
        req.setStock(20);
        req.setActive(true);
        return req;
    }

    @Test void create_requestNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminProductService.create(null));
    }

    @Test void update_productIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminProductService.update(null, validRequest()));
    }

    @Test void toggleActive_productIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminProductService.toggleActive(null));
    }

    @Test void delete_productIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminProductService.delete(null));
    }

    @Test void create_nameBlank_shouldThrow_PRE() {
        AdminProductRequest req = validRequest();
        req.setName("   ");

        assertThrows(ContractViolationException.class,
                     ()->adminProductService.create(req));
    }

    @Test void create_priceNegative_shouldThrow_PRE() {
        AdminProductRequest req = validRequest();
        req.setPrice(-1);

        assertThrows(ContractViolationException.class,
                     ()->adminProductService.create(req));
    }

    @Test void create_stockNegative_shouldThrow_PRE() {
        AdminProductRequest req = validRequest();
        req.setStock(-2);

        assertThrows(ContractViolationException.class,
                     ()->adminProductService.create(req));
    }

    @Test void create_valid_shouldSaveProduct() {
        AdminProductRequest req = validRequest();

        when(productRepo.save(any(Product.class))).thenAnswer(inv->{
            Product p = inv.getArgument(0);
            Product saved = new Product();
            saved.setName(p.getName());
            saved.setDescription(p.getDescription());
            saved.setCategory(p.getCategory());
            saved.setImageUrl(p.getImageUrl());
            saved.setPrice(p.getPrice());
            saved.setStock(p.getStock());
            saved.setActive(p.isActive());

            try {
                var f = Product.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(saved, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return saved;
        });

        Product result = adminProductService.create(req);

        assertNotNull(result.getId());
        assertEquals("Áo thun local brand", result.getName());
        assertEquals(199000, result.getPrice());
        assertEquals(20, result.getStock());
        verify(productRepo).save(any(Product.class));
    }

    @Test void update_productNotFound_shouldThrow_PRE() {
        AdminProductRequest req = validRequest();
        when(productRepo.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->adminProductService.update(1L, req));
    }

    @Test void update_valid_shouldUpdateProduct() {
        AdminProductRequest req = validRequest();
        req.setName("Tên mới");
        req.setPrice(250000);

        Product existing = new Product();
        existing.setName("Tên cũ");
        existing.setDescription("Mô tả cũ");
        existing.setCategory("Fashion");
        existing.setImageUrl("");
        existing.setPrice(100000);
        existing.setStock(5);
        existing.setActive(true);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(existing, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(productRepo.save(any(Product.class))).thenAnswer(inv->inv.getArgument(0));

        Product updated = adminProductService.update(1L, req);

        assertEquals("Tên mới", updated.getName());
        assertEquals(250000, updated.getPrice());
        verify(productRepo).save(existing);
    }

    @Test void toggleActive_shouldFlipStatus() {
        Product p = new Product();
        p.setName("SP");
        p.setPrice(100);
        p.setStock(1);
        p.setActive(true);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(p));

        adminProductService.toggleActive(1L);

        assertFalse(p.isActive());
        verify(productRepo).save(p);
    }

    @Test void delete_productNotFound_shouldThrow_PRE() {
        when(productRepo.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->adminProductService.delete(99L));
    }

    @Test void delete_valid_shouldDeleteProduct() {
        Product p = new Product();
        p.setName("SP");
        p.setPrice(100);
        p.setStock(1);

        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(p));

        adminProductService.delete(1L);

        verify(productRepo).delete(p);
    }
}
