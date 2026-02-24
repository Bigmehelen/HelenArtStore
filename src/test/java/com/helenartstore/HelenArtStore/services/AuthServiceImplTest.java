package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class AuthServiceImplTest {

        @Mock
        private UserRepository userRepository;
        @Mock
        private CustomUserDetailsService customUserDetailsService;
        @Mock
        private AuthenticationManager authenticationManager;
        @Mock
        private JwtService jwtService;
        @Mock
        private UserMapper userMapper;
        @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private AuthServiceImpl authService;

        private User user;

        @BeforeEach
        void setUp() {
                user = User.builder()
                                .id(1L)
                                .username("testuser")
                                .email("test@example.com")
                                .password("password")
                                .roles(new HashSet<>(Set.of(Role.USER)))
                                .build();

        }

        @Test
        void upgradeToArtist_Success() {
                when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
                when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
                when(customUserDetailsService.loadUserByUsername("testuser")).thenReturn(user);
                when(jwtService.generateToken(any(UserDetails.class))).thenReturn("new_token");

                AuthResponse response = authService.upgradeToArtist("testuser");

                assertNotNull(response);
                assertTrue(response.getRoles().contains(Role.ARTIST));
                assertEquals("new_token", response.getToken());
                verify(userRepository).save(user);
        }

        @Test
        void upgradeToArtist_AlreadyArtist() {
                user.getRoles().add(Role.ARTIST);
                when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

                assertThrows(com.helenartstore.HelenArtStore.exceptions.UserAlreadyArtistException.class,
                                () -> authService.upgradeToArtist("testuser"));
                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void upgradeToArtist_UserNotFound() {
                when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

                assertThrows(com.helenartstore.HelenArtStore.exceptions.UserNotFoundException.class,
                                () -> authService.upgradeToArtist("unknown"));
        }
}
