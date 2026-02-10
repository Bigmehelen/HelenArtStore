package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
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

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        request = new RegisterRequest(
                "testname",
                "testemail@example.com",
                "password"
                );

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
        User user = userMapper.INSTANCE.map(request);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(customUserDetailsService.loadUserByUsername(request.username()))
                .thenReturn(new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        List.of(
                                new SimpleGrantedAuthority("ROLE_" + Role.USER.name())
                        )
                ));
        when(jwtService.generateToken(any())).thenReturn("token");
        response = authServiceImpl.register(request);
        assertThat(response).isNotNull();
    }


}
