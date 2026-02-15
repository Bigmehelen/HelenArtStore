package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AuthServiceImplTest {
    RegisterRequest request;
    LoginRequest loginRequest;
    AuthResponse response;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        request = new RegisterRequest();
        request.setUsername("testname");
        request.setEmail("testemail@gmail.com");
        request.setPassword("testpassword");

        loginRequest = new LoginRequest();
             loginRequest.setUsername("testname");
             loginRequest.setPassword("testpassword");

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
        User user = loadCorrectUser();

        when(userMapper.toEntity(any(RegisterRequest.class)))
                .thenReturn(user);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(customUserDetailsService.loadUserByUsername(request.getUsername()))
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

    @Test
    void testThatUserCanLoginWithCorrectCredentials() {

        User user = loadCorrectUser();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                ));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        when(customUserDetailsService.loadUserByUsername(user.getUsername()))
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails)).thenReturn("login-token");

        AuthResponse response = authServiceImpl.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("login-token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }



    private User loadCorrectUser() {
        User user = new User();
        user.setUsername("testname");
        user.setEmail("testemail@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        return user;
    }
}
