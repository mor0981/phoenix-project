package com.phoenix.project.security;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.repository.ClientRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public CustomUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] parts = username.split(":", 2);
        if (parts.length != 2) throw new UsernameNotFoundException("Invalid username format");

        Client.ContactMethodType type;
        try {
            type = Client.ContactMethodType.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Unknown contact method: " + parts[0]);
        }

        Client client = clientRepository
                .findByContactMethodTypeAndMethodValue(type, parts[1])
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new ClientUserDetails(client);
    }

    public static class ClientUserDetails implements UserDetails {
        private final Client client;

        public ClientUserDetails(Client client) { this.client = client; }

        public Client getClient() { return client; }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + client.getRole().name()));
        }

        @Override public String getPassword() { return client.getPassword(); }
        @Override public String getUsername() { return client.getContactMethodType().name() + ":" + client.getMethodValue(); }
        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return !client.isBlocked(); }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return true; }
    }
}
