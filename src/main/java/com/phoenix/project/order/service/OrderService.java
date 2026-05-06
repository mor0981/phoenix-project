package com.phoenix.project.order.service;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.service.ClientService;
import com.phoenix.project.common.exception.BusinessException;
import com.phoenix.project.common.exception.ConflictException;
import com.phoenix.project.order.dto.OrderDto;
import com.phoenix.project.order.entity.Order;
import com.phoenix.project.order.repository.OrderRepository;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ClientService clientService;

    public OrderService(OrderRepository orderRepository, ProductService productService, ClientService clientService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.clientService = clientService;
    }

    @Transactional
    public OrderDto.OrderResponse purchaseProduct(Long productId, Authentication auth) {
        Client buyer = clientService.getCurrentClient(auth);
        Product product = productService.getProductOrThrow(productId);

        if (product.getSeller().getId().equals(buyer.getId())) {
            throw new BusinessException("You cannot purchase your own product");
        }
        if (product.getStatus() != Product.ProductStatus.AVAILABLE) {
            throw new BusinessException("Product is not available for purchase");
        }
        if (orderRepository.existsByBuyerAndProduct(buyer, product)) {
            throw new ConflictException("You have already purchased this product");
        }

        product.setStatus(Product.ProductStatus.SOLD);

        Order order = Order.builder()
                .buyer(buyer)
                .product(product)
                .status(Order.OrderStatus.COMPLETED)
                .priceAtPurchase(product.getPrice())
                .build();

        return OrderDto.OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderDto.OrderResponse> getMyOrders(Authentication auth) {
        Client buyer = clientService.getCurrentClient(auth);
        return orderRepository.findByBuyer(buyer).stream()
                .map(OrderDto.OrderResponse::from).toList();
    }
}
