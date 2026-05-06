package com.phoenix.project.product;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.service.ClientService;
import com.phoenix.project.common.exception.ResourceNotFoundException;
import com.phoenix.project.product.dto.ProductDto;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.repository.ProductRepository;
import com.phoenix.project.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private Authentication authentication;

    private ProductService productService;

    private Client owner;
    private Client otherClient;
    private Product product;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, clientService);

        owner = Client.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .role(Client.Role.USER)
                .blocked(false)
                .build();

        otherClient = Client.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Jones")
                .role(Client.Role.USER)
                .blocked(false)
                .build();

        product = Product.builder()
                .id(1L)
                .title("Test Product")
                .description("A test product")
                .price(BigDecimal.valueOf(99.99))
                .category("electronics")
                .status(Product.ProductStatus.AVAILABLE)
                .seller(owner)
                .build();
    }

    @Test
    void createProduct_success() {
        ProductDto.CreateProductRequest request = new ProductDto.CreateProductRequest();
        request.setTitle("New Product");
        request.setPrice(BigDecimal.valueOf(50.00));

        Product savedProduct = Product.builder()
                .id(2L)
                .title("New Product")
                .price(BigDecimal.valueOf(50.00))
                .status(Product.ProductStatus.AVAILABLE)
                .seller(owner)
                .build();

        when(clientService.getCurrentClient(authentication)).thenReturn(owner);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto.ProductResponse response = productService.createProduct(request, authentication);

        assertNotNull(response);
        assertEquals("New Product", response.getTitle());
        assertEquals("AVAILABLE", response.getStatus());
    }

    @Test
    void updateProduct_ownProduct_success() {
        ProductDto.UpdateProductRequest request = new ProductDto.UpdateProductRequest();
        request.setTitle("Updated Title");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(clientService.getCurrentClient(authentication)).thenReturn(owner);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto.ProductResponse response = productService.updateProduct(1L, request, authentication);

        assertNotNull(response);
        verify(productRepository).save(any());
    }

    @Test
    void updateProduct_foreignProduct_throwsAccessDenied() {
        ProductDto.UpdateProductRequest request = new ProductDto.UpdateProductRequest();
        request.setTitle("Hacked Title");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(clientService.getCurrentClient(authentication)).thenReturn(otherClient);

        assertThrows(AccessDeniedException.class,
                () -> productService.updateProduct(1L, request, authentication));
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_notFound_throwsResourceNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductDto.UpdateProductRequest request = new ProductDto.UpdateProductRequest();
        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(99L, request, authentication));
    }

    @Test
    void deleteProduct_foreignProduct_throwsAccessDenied() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(clientService.getCurrentClient(authentication)).thenReturn(otherClient);

        assertThrows(AccessDeniedException.class,
                () -> productService.deleteProduct(1L, authentication));
    }
}
