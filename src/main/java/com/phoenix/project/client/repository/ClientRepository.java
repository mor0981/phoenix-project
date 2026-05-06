package com.phoenix.project.client.repository;

import com.phoenix.project.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByContactMethodTypeAndMethodValue(
        Client.ContactMethodType contactMethodType,
        String methodValue
    );

    boolean existsByContactMethodTypeAndMethodValue(
        Client.ContactMethodType contactMethodType,
        String methodValue
    );

    long countByBlocked(boolean blocked);
}
