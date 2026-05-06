package com.phoenix.project.client.dto;

import com.phoenix.project.client.entity.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientDto {

    public static class ClientResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private Client.ContactMethodType contactMethodType;
        private String methodValue;
        private String role;
        private boolean blocked;
        private LocalDateTime createdAt;

        public static ClientResponse from(Client client) {
            ClientResponse r = new ClientResponse();
            r.id = client.getId();
            r.firstName = client.getFirstName();
            r.lastName = client.getLastName();
            r.birthDate = client.getBirthDate();
            r.contactMethodType = client.getContactMethodType();
            r.methodValue = client.getMethodValue();
            r.role = client.getRole().name();
            r.blocked = client.isBlocked();
            r.createdAt = client.getCreatedAt();
            return r;
        }

        public Long getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public LocalDate getBirthDate() { return birthDate; }
        public Client.ContactMethodType getContactMethodType() { return contactMethodType; }
        public String getMethodValue() { return methodValue; }
        public String getRole() { return role; }
        public boolean isBlocked() { return blocked; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
}
