package com.phoenix.project.auth.service;

import com.phoenix.project.auth.dto.AuthDto;
import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.repository.ClientRepository;
import com.phoenix.project.common.exception.ConflictException;
import com.phoenix.project.common.exception.UnauthorizedException;
import com.phoenix.project.security.CustomUserDetailsService;
import com.phoenix.project.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (clientRepository.existsByContactMethodTypeAndMethodValue(
                request.getContactMethodType(), request.getMethodValue())) {
            throw new ConflictException("User already exists with this contact method");
        }

        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .contactMethodType(request.getContactMethodType())
                .methodValue(request.getMethodValue())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Client.Role.USER)
                .blocked(false)
                .build();

        clientRepository.save(client);

        CustomUserDetailsService.ClientUserDetails userDetails =
                new CustomUserDetailsService.ClientUserDetails(client);
        String token = jwtService.generateToken(userDetails);

        return new AuthDto.AuthResponse(
                token, client.getId(), client.getFirstName(), client.getLastName(), client.getRole().name()
        );
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        Client client = clientRepository
                .findByContactMethodTypeAndMethodValue(
                        request.getContactMethodType(), request.getMethodValue())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (client.isBlocked()) {
            throw new UnauthorizedException("Account is blocked");
        }

        if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        CustomUserDetailsService.ClientUserDetails userDetails =
                new CustomUserDetailsService.ClientUserDetails(client);
        String token = jwtService.generateToken(userDetails);

        return new AuthDto.AuthResponse(
                token, client.getId(), client.getFirstName(), client.getLastName(), client.getRole().name()
        );
    }
}
