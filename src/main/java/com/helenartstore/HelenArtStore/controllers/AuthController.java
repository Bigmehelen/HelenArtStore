package com.helenartstore.HelenArtStore.controllers;

import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/become-artist")
    public ResponseEntity<AuthResponse> becomeArtist(org.springframework.security.core.Authentication authentication) {
        // In a real scenario, we get the username from the SecurityContext
        // We assume the user is authenticated via JWT filter before hitting this
        // endpoint.
        // However, standard Spring Security usage usually involves injecting
        // Authentication or Principal.
        // If the endpoint is secured, 'authentication' will be populated.

        String username = authentication != null ? authentication.getName() : null;
        AuthResponse response = authService.upgradeToArtist(username);
        return ResponseEntity.ok(response);
    }
}
