package com.phoenix.project.auth.dto;

import com.phoenix.project.client.entity.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AuthDto {

    public static class RegisterRequest {
        @NotBlank private String firstName;
        @NotBlank private String lastName;
        private LocalDate birthDate;
        @NotNull private Client.ContactMethodType contactMethodType;
        @NotBlank private String methodValue;
        @NotBlank private String password;

        public String getFirstName() { return firstName; }
        public void setFirstName(String v) { this.firstName = v; }
        public String getLastName() { return lastName; }
        public void setLastName(String v) { this.lastName = v; }
        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate v) { this.birthDate = v; }
        public Client.ContactMethodType getContactMethodType() { return contactMethodType; }
        public void setContactMethodType(Client.ContactMethodType v) { this.contactMethodType = v; }
        public String getMethodValue() { return methodValue; }
        public void setMethodValue(String v) { this.methodValue = v; }
        public String getPassword() { return password; }
        public void setPassword(String v) { this.password = v; }
    }

    public static class LoginRequest {
        @NotNull private Client.ContactMethodType contactMethodType;
        @NotBlank private String methodValue;
        @NotBlank private String password;

        public Client.ContactMethodType getContactMethodType() { return contactMethodType; }
        public void setContactMethodType(Client.ContactMethodType v) { this.contactMethodType = v; }
        public String getMethodValue() { return methodValue; }
        public void setMethodValue(String v) { this.methodValue = v; }
        public String getPassword() { return password; }
        public void setPassword(String v) { this.password = v; }
    }

    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private Long clientId;
        private String firstName;
        private String lastName;
        private String role;

        public AuthResponse(String token, Long clientId, String firstName, String lastName, String role) {
            this.token = token; this.clientId = clientId; this.firstName = firstName;
            this.lastName = lastName; this.role = role;
        }

        public String getToken() { return token; }
        public String getType() { return type; }
        public Long getClientId() { return clientId; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getRole() { return role; }
    }
}
