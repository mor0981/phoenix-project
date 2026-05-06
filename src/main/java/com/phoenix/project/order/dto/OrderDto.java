package com.phoenix.project.order.dto;

import com.phoenix.project.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {

    public static class OrderResponse {
        private Long id;
        private Long buyerId;
        private String buyerName;
        private Long productId;
        private String productTitle;
        private String status;
        private BigDecimal priceAtPurchase;
        private LocalDateTime createdAt;

        public static OrderResponse from(Order o) {
            OrderResponse r = new OrderResponse();
            r.id = o.getId();
            r.buyerId = o.getBuyer().getId();
            r.buyerName = o.getBuyer().getFirstName() + " " + o.getBuyer().getLastName();
            r.productId = o.getProduct().getId();
            r.productTitle = o.getProduct().getTitle();
            r.status = o.getStatus().name();
            r.priceAtPurchase = o.getPriceAtPurchase();
            r.createdAt = o.getCreatedAt();
            return r;
        }

        public Long getId() { return id; }
        public Long getBuyerId() { return buyerId; }
        public String getBuyerName() { return buyerName; }
        public Long getProductId() { return productId; }
        public String getProductTitle() { return productTitle; }
        public String getStatus() { return status; }
        public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
}
