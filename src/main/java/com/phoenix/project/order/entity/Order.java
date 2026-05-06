package com.phoenix.project.order.entity;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.product.entity.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "orders",
    uniqueConstraints = @UniqueConstraint(columnNames = {"buyer_id", "product_id"})
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Client buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal priceAtPurchase;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum OrderStatus { COMPLETED, CANCELLED }

    public Order() {}

    private Order(Builder b) {
        this.buyer = b.buyer; this.product = b.product;
        this.status = b.status; this.priceAtPurchase = b.priceAtPurchase;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Client buyer; private Product product;
        private OrderStatus status; private BigDecimal priceAtPurchase;

        public Builder id(Long v) { this.id = v; return this; }
        public Builder buyer(Client v) { this.buyer = v; return this; }
        public Builder product(Product v) { this.product = v; return this; }
        public Builder status(OrderStatus v) { this.status = v; return this; }
        public Builder priceAtPurchase(BigDecimal v) { this.priceAtPurchase = v; return this; }
        public Order build() { return new Order(this); }
    }

    public Long getId() { return id; }
    public Client getBuyer() { return buyer; }
    public Product getProduct() { return product; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
