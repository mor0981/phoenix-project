package com.phoenix.project.admin.dto;

import com.phoenix.project.client.entity.Client;
import jakarta.validation.constraints.NotNull;

public class AdminDto {

    public static class UpdateRoleRequest {
        @NotNull
        private Client.Role role;

        public Client.Role getRole() { return role; }
        public void setRole(Client.Role role) { this.role = role; }
    }

    public static class StatisticsResponse {
        private long totalUsers;
        private long blockedUsers;
        private long totalProducts;
        private long availableProducts;
        private long soldProducts;
        private long totalOrders;

        public StatisticsResponse(long totalUsers, long blockedUsers, long totalProducts,
                                  long availableProducts, long soldProducts, long totalOrders) {
            this.totalUsers = totalUsers;
            this.blockedUsers = blockedUsers;
            this.totalProducts = totalProducts;
            this.availableProducts = availableProducts;
            this.soldProducts = soldProducts;
            this.totalOrders = totalOrders;
        }

        public long getTotalUsers() { return totalUsers; }
        public long getBlockedUsers() { return blockedUsers; }
        public long getTotalProducts() { return totalProducts; }
        public long getAvailableProducts() { return availableProducts; }
        public long getSoldProducts() { return soldProducts; }
        public long getTotalOrders() { return totalOrders; }
    }
}
