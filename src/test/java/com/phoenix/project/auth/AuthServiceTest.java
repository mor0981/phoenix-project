package com.phoenix.project.auth;

import com.phoenix.project.auth.dto.AuthDto;
import com.phoenix.project.auth.service.AuthService;
import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.repository.ClientRepository;
import com.phoenix.project.common.exception.ConflictException;
import com.phoenix.project.common.exception.UnauthorizedException;
import com.phoenix.project.security.CustomUserDetailsService;
import com.phoenix.project.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    private AuthDto.RegisterRequest registerRequest;
    private Client existingClient;

    @BeforeEach
    void setUp() {
        authService = new AuthService(clientRepository, passwordEncoder, jwtService);

        registerRequest = new AuthDto.RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setContactMethodType(Client.ContactMethodType.EMAIL);
        registerRequest.setMethodValue("john@example.com");
        registerRequest.setPassword("password123");

        existingClient = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .contactMethodType(Client.ContactMethodType.EMAIL)
                .methodValue("john@example.com")
                .password("encodedPassword")
                .role(Client.Role.USER)
                .blocked(false)
                .build();
    }

    @Test
    void register_success() {
        when(clientRepository.existsByContactMethodTypeAndMethodValue(any(), anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthDto.AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void register_duplicateUser_throwsConflict() {
        when(clientRepository.existsByContactMethodTypeAndMethodValue(any(), anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(registerRequest));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void login_success() {
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();
        loginRequest.setContactMethodType(Client.ContactMethodType.EMAIL);
        loginRequest.setMethodValue("john@example.com");
        loginRequest.setPassword("password123");

        when(clientRepository.findByContactMethodTypeAndMethodValue(any(), anyString()))
                .thenReturn(Optional.of(existingClient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthDto.AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_wrongPassword_throwsUnauthorized() {
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();
        loginRequest.setContactMethodType(Client.ContactMethodType.EMAIL);
        loginRequest.setMethodValue("john@example.com");
        loginRequest.setPassword("wrongPassword");

        when(clientRepository.findByContactMethodTypeAndMethodValue(any(), anyString()))
                .thenReturn(Optional.of(existingClient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_blockedUser_throwsUnauthorized() {
        existingClient.setBlocked(true);

        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();
        loginRequest.setContactMethodType(Client.ContactMethodType.EMAIL);
        loginRequest.setMethodValue("john@example.com");
        loginRequest.setPassword("password123");

        when(clientRepository.findByContactMethodTypeAndMethodValue(any(), anyString()))
                .thenReturn(Optional.of(existingClient));

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }
}
