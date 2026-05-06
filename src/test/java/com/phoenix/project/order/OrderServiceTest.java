package com.phoenix.project.order;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.service.ClientService;
import com.phoenix.project.common.exception.BusinessException;
import com.phoenix.project.common.exception.ConflictException;
import com.phoenix.project.order.dto.OrderDto;
import com.phoenix.project.order.entity.Order;
import com.phoenix.project.order.repository.OrderRepository;
import com.phoenix.project.order.service.OrderService;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ClientService clientService;

    @Mock
    private Authentication authentication;

    private OrderService orderService;

    private Client buyer;
    private Client seller;
    private Product availableProduct;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productService, clientService);

        seller = Client.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Seller")
                .role(Client.Role.USER)
                .build();

        buyer = Client.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Buyer")
                .role(Client.Role.USER)
                .build();

        availableProduct = Product.builder()
                .id(10L)
                .title("Cool Gadget")
                .price(BigDecimal.valueOf(199.99))
                .status(Product.ProductStatus.AVAILABLE)
                .seller(seller)
                .build();
    }

    @Test
    void purchaseProduct_success() {
        Order savedOrder = Order.builder()
                .buyer(buyer)
                .product(availableProduct)
                .status(Order.OrderStatus.COMPLETED)
                .priceAtPurchase(availableProduct.getPrice())
                .build();

        when(clientService.getCurrentClient(authentication)).thenReturn(buyer);
        when(productService.getProductOrThrow(10L)).thenReturn(availableProduct);
        when(orderRepository.existsByBuyerAndProduct(buyer, availableProduct)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDto.OrderResponse response = orderService.purchaseProduct(10L, authentication);

        assertNotNull(response);
        assertEquals(Order.OrderStatus.COMPLETED.name(), response.getStatus());
        assertEquals(Product.ProductStatus.SOLD, availableProduct.getStatus());
    }

    @Test
    void purchaseProduct_alreadyPurchased_throwsConflict() {
        when(clientService.getCurrentClient(authentication)).thenReturn(buyer);
        when(productService.getProductOrThrow(10L)).thenReturn(availableProduct);
        when(orderRepository.existsByBuyerAndProduct(buyer, availableProduct)).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> orderService.purchaseProduct(10L, authentication));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void purchaseProduct_productNotAvailable_throwsBusiness() {
        availableProduct.setStatus(Product.ProductStatus.SOLD);

        when(clientService.getCurrentClient(authentication)).thenReturn(buyer);
        when(productService.getProductOrThrow(10L)).thenReturn(availableProduct);

        assertThrows(BusinessException.class,
                () -> orderService.purchaseProduct(10L, authentication));
    }

    @Test
    void purchaseProduct_ownProduct_throwsBusiness() {
        when(clientService.getCurrentClient(authentication)).thenReturn(seller);
        when(productService.getProductOrThrow(10L)).thenReturn(availableProduct);

        assertThrows(BusinessException.class,
                () -> orderService.purchaseProduct(10L, authentication));
    }
}
