package com.phoenix.project.order.repository;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.order.entity.Order;
import com.phoenix.project.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyer(Client buyer);

    boolean existsByBuyerAndProduct(Client buyer, Product product);
}
