package com.phoenix.project.product.service;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.service.ClientService;
import com.phoenix.project.common.exception.ResourceNotFoundException;
import com.phoenix.project.product.dto.ProductDto;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.repository.ProductRepository;
import com.phoenix.project.product.repository.ProductSpecification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ClientService clientService;

    public ProductService(ProductRepository productRepository, ClientService clientService) {
        this.productRepository = productRepository;
        this.clientService = clientService;
    }

    @Transactional
    public ProductDto.ProductResponse createProduct(ProductDto.CreateProductRequest request, Authentication auth) {
        Client seller = clientService.getCurrentClient(auth);
        Product product = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .status(Product.ProductStatus.AVAILABLE)
                .seller(seller)
                .build();
        return ProductDto.ProductResponse.from(productRepository.save(product));
    }

    public List<ProductDto.ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDto.ProductResponse::from).toList();
    }

    public List<ProductDto.ProductResponse> getMyProducts(Authentication auth) {
        Client seller = clientService.getCurrentClient(auth);
        return productRepository.findBySeller(seller).stream()
                .map(ProductDto.ProductResponse::from).toList();
    }

    public List<ProductDto.ProductResponse> searchProducts(String name, String category,
            BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status) {
        return productRepository
                .findAll(ProductSpecification.withFilters(name, category, minPrice, maxPrice, status))
                .stream().map(ProductDto.ProductResponse::from).toList();
    }

    @Transactional
    public ProductDto.ProductResponse updateProduct(Long id, ProductDto.UpdateProductRequest request, Authentication auth) {
        Product product = getProductOrThrow(id);
        Client currentUser = clientService.getCurrentClient(auth);
        validateOwnership(product, currentUser);
        if (request.getTitle() != null) product.setTitle(request.getTitle());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getCategory() != null) product.setCategory(request.getCategory());
        return ProductDto.ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id, Authentication auth) {
        Product product = getProductOrThrow(id);
        Client currentUser = clientService.getCurrentClient(auth);
        validateOwnership(product, currentUser);
        product.setStatus(Product.ProductStatus.REMOVED);
        productRepository.save(product);
    }

    public Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private void validateOwnership(Product product, Client user) {
        if (!product.getSeller().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only modify your own products");
        }
    }
}
