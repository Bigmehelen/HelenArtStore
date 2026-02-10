package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AuthServiceImplTest {
    RegisterRequest request;
    AuthResponse response;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @InjectMocks
    private JwtService jwtService;

//    @InjectMocks
//    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        request = new RegisterRequest(
                "testname",
                "testemail@example.com",
                "password",
                "testfirstname",
                "testlastname");



        response = new AuthResponse("token",
                 "Bearer",
                 1L,
                 "testname",
                 "testemail@example.com",
                    Role.USER
                 );
    }

    @Test
    void testThatUserCanRegisterWithCorrectCredentials() {
        when(userRepository.count()).thenReturn(0L);
        User user = Mapper.map(request);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.username());
        when(jwtService.generateToken(any())).thenReturn("token");
        response = authServiceImpl.register(request);
        assertThat(response).isNotNull();
    }


}
