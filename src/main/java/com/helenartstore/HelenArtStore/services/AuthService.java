package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse upgradeToArtist(String username);
}
