package com.phoenix.project.product.repository;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    List<Product> findBySeller(Client seller);

    long countByStatus(Product.ProductStatus status);
}
