package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {
    LoginRequest request;
    AuthResponse response;
    RegisterRequest registerRequest;
    AuthResponse registerResponse;

   @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Test
    void registerUserCountIsOne(){
       response = authservice.register()
    }
}
