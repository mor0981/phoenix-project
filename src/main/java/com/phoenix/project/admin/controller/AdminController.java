package com.phoenix.project.admin.controller;

import com.phoenix.project.admin.dto.AdminDto;
import com.phoenix.project.admin.service.AdminService;
import com.phoenix.project.client.dto.ClientDto;
import com.phoenix.project.order.dto.OrderDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) { this.adminService = adminService; }

    @GetMapping("/users")
    public ResponseEntity<List<ClientDto.ClientResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<ClientDto.ClientResponse> blockUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.blockUser(id));
    }

    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<ClientDto.ClientResponse> unblockUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.unblockUser(id));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ClientDto.ClientResponse> updateUserRole(
            @PathVariable Long id, @Valid @RequestBody AdminDto.UpdateRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(id, request));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto.OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(adminService.getAllOrders());
    }

    @GetMapping("/statistics")
    public ResponseEntity<AdminDto.StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(adminService.getStatistics());
    }
}
