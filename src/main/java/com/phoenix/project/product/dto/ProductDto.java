package com.phoenix.project.product.dto;

import com.phoenix.project.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {

    public static class CreateProductRequest {
        @NotBlank private String title;
        private String description;
        @NotNull @Positive private BigDecimal price;
        private String category;

        public String getTitle() { return title; }
        public void setTitle(String v) { this.title = v; }
        public String getDescription() { return description; }
        public void setDescription(String v) { this.description = v; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal v) { this.price = v; }
        public String getCategory() { return category; }
        public void setCategory(String v) { this.category = v; }
    }

    public static class UpdateProductRequest {
        private String title;
        private String description;
        @Positive private BigDecimal price;
        private String category;

        public String getTitle() { return title; }
        public void setTitle(String v) { this.title = v; }
        public String getDescription() { return description; }
        public void setDescription(String v) { this.description = v; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal v) { this.price = v; }
        public String getCategory() { return category; }
        public void setCategory(String v) { this.category = v; }
    }

    public static class ProductResponse {
        private Long id;
        private String title;
        private String description;
        private BigDecimal price;
        private String category;
        private String status;
        private Long sellerId;
        private String sellerName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ProductResponse from(Product p) {
            ProductResponse r = new ProductResponse();
            r.id = p.getId();
            r.title = p.getTitle();
            r.description = p.getDescription();
            r.price = p.getPrice();
            r.category = p.getCategory();
            r.status = p.getStatus().name();
            r.sellerId = p.getSeller().getId();
            r.sellerName = p.getSeller().getFirstName() + " " + p.getSeller().getLastName();
            r.createdAt = p.getCreatedAt();
            r.updatedAt = p.getUpdatedAt();
            return r;
        }

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public String getCategory() { return category; }
        public String getStatus() { return status; }
        public Long getSellerId() { return sellerId; }
        public String getSellerName() { return sellerName; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
    }
}
