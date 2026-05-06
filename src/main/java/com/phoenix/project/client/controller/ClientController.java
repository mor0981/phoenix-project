package com.phoenix.project.client.controller;

import com.phoenix.project.client.dto.ClientDto;
import com.phoenix.project.client.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) { this.clientService = clientService; }

    @GetMapping("/me")
    public ResponseEntity<ClientDto.ClientResponse> getMe(Authentication authentication) {
        return ResponseEntity.ok(clientService.getMe(authentication));
    }
}
