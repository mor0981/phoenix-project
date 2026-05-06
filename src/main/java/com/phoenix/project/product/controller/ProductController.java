package com.phoenix.project.product.controller;

import com.phoenix.project.product.dto.ProductDto;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) { this.productService = productService; }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductDto.ProductResponse> createProduct(
            @Valid @RequestBody ProductDto.CreateProductRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request, auth));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto.ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto.ProductResponse>> getMyProducts(Authentication auth) {
        return ResponseEntity.ok(productService.getMyProducts(auth));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto.ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Product.ProductStatus status) {
        return ResponseEntity.ok(productService.searchProducts(name, category, minPrice, maxPrice, status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductDto.ProductResponse> updateProduct(
            @PathVariable Long id, @RequestBody ProductDto.UpdateProductRequest request, Authentication auth) {
        return ResponseEntity.ok(productService.updateProduct(id, request, auth));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Authentication auth) {
        productService.deleteProduct(id, auth);
        return ResponseEntity.noContent().build();
    }
}
