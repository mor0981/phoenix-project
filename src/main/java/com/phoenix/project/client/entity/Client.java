package com.phoenix.project.client.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "clients",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"contact_method_type", "method_value"}
    )
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_method_type", nullable = false)
    private ContactMethodType contactMethodType;

    @Column(name = "method_value", nullable = false)
    private String methodValue;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean blocked = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ContactMethodType { EMAIL, PHONE, USERNAME }
    public enum Role { USER, ADMIN }

    public Client() {}

    private Client(Builder b) {
        this.id = b.id; this.firstName = b.firstName; this.lastName = b.lastName;
        this.birthDate = b.birthDate; this.contactMethodType = b.contactMethodType;
        this.methodValue = b.methodValue; this.password = b.password;
        this.role = b.role; this.blocked = b.blocked;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String firstName; private String lastName;
        private LocalDate birthDate; private ContactMethodType contactMethodType;
        private String methodValue; private String password; private Role role;
        private boolean blocked = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder firstName(String v) { this.firstName = v; return this; }
        public Builder lastName(String v) { this.lastName = v; return this; }
        public Builder birthDate(LocalDate v) { this.birthDate = v; return this; }
        public Builder contactMethodType(ContactMethodType v) { this.contactMethodType = v; return this; }
        public Builder methodValue(String v) { this.methodValue = v; return this; }
        public Builder password(String v) { this.password = v; return this; }
        public Builder role(Role v) { this.role = v; return this; }
        public Builder blocked(boolean v) { this.blocked = v; return this; }
        public Client build() { return new Client(this); }
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public ContactMethodType getContactMethodType() { return contactMethodType; }
    public String getMethodValue() { return methodValue; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public boolean isBlocked() { return blocked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setFirstName(String v) { this.firstName = v; }
    public void setLastName(String v) { this.lastName = v; }
    public void setBirthDate(LocalDate v) { this.birthDate = v; }
    public void setContactMethodType(ContactMethodType v) { this.contactMethodType = v; }
    public void setMethodValue(String v) { this.methodValue = v; }
    public void setPassword(String v) { this.password = v; }
    public void setRole(Role v) { this.role = v; }
    public void setBlocked(boolean v) { this.blocked = v; }
}
