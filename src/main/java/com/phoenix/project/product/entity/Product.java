package com.phoenix.project.product.entity;

import com.phoenix.project.client.entity.Client;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Client seller;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public enum ProductStatus { AVAILABLE, SOLD, REMOVED, BLOCKED }

    public Product() {}

    private Product(Builder b) {
        this.id = b.id; this.title = b.title; this.description = b.description;
        this.price = b.price; this.category = b.category; this.status = b.status;
        this.seller = b.seller;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String title; private String description;
        private BigDecimal price; private String category; private ProductStatus status;
        private Client seller;

        public Builder id(Long v) { this.id = v; return this; }
        public Builder title(String v) { this.title = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder price(BigDecimal v) { this.price = v; return this; }
        public Builder category(String v) { this.category = v; return this; }
        public Builder status(ProductStatus v) { this.status = v; return this; }
        public Builder seller(Client v) { this.seller = v; return this; }
        public Product build() { return new Product(this); }
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public ProductStatus getStatus() { return status; }
    public Client getSeller() { return seller; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    public void setTitle(String v) { this.title = v; }
    public void setDescription(String v) { this.description = v; }
    public void setPrice(BigDecimal v) { this.price = v; }
    public void setCategory(String v) { this.category = v; }
    public void setStatus(ProductStatus v) { this.status = v; }
    public void setSeller(Client v) { this.seller = v; }
}
