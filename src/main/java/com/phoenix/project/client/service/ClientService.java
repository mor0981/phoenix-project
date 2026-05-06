package com.phoenix.project.client.service;

import com.phoenix.project.client.dto.ClientDto;
import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.repository.ClientRepository;
import com.phoenix.project.common.exception.ResourceNotFoundException;
import com.phoenix.project.security.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDto.ClientResponse getMe(Authentication authentication) {
        return ClientDto.ClientResponse.from(getCurrentClient(authentication));
    }

    public Client getCurrentClient(Authentication authentication) {
        CustomUserDetailsService.ClientUserDetails userDetails =
                (CustomUserDetailsService.ClientUserDetails) authentication.getPrincipal();
        return userDetails.getClient();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }
}
