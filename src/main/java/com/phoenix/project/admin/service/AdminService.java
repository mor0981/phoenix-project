package com.phoenix.project.admin.service;

import com.phoenix.project.admin.dto.AdminDto;
import com.phoenix.project.client.dto.ClientDto;
import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.repository.ClientRepository;
import com.phoenix.project.common.exception.ResourceNotFoundException;
import com.phoenix.project.order.dto.OrderDto;
import com.phoenix.project.order.repository.OrderRepository;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.repository.ProductRepository;
import com.phoenix.project.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public AdminService(ClientRepository clientRepository, ProductRepository productRepository,
                        OrderRepository orderRepository, ProductService productService) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public List<ClientDto.ClientResponse> getAllUsers() {
        return clientRepository.findAll().stream().map(ClientDto.ClientResponse::from).toList();
    }

    @Transactional
    public ClientDto.ClientResponse blockUser(Long id) {
        Client client = getClientOrThrow(id);
        client.setBlocked(true);
        return ClientDto.ClientResponse.from(clientRepository.save(client));
    }

    @Transactional
    public ClientDto.ClientResponse unblockUser(Long id) {
        Client client = getClientOrThrow(id);
        client.setBlocked(false);
        return ClientDto.ClientResponse.from(clientRepository.save(client));
    }

    @Transactional
    public ClientDto.ClientResponse updateUserRole(Long id, AdminDto.UpdateRoleRequest request) {
        Client client = getClientOrThrow(id);
        client.setRole(request.getRole());
        return ClientDto.ClientResponse.from(clientRepository.save(client));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productService.getProductOrThrow(productId);
        product.setStatus(Product.ProductStatus.BLOCKED);
        productRepository.save(product);
    }

    public List<OrderDto.OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderDto.OrderResponse::from).toList();
    }

    public AdminDto.StatisticsResponse getStatistics() {
        return new AdminDto.StatisticsResponse(
                clientRepository.count(),
                clientRepository.countByBlocked(true),
                productRepository.count(),
                productRepository.countByStatus(Product.ProductStatus.AVAILABLE),
                productRepository.countByStatus(Product.ProductStatus.SOLD),
                orderRepository.count()
        );
    }

    private Client getClientOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }
}
