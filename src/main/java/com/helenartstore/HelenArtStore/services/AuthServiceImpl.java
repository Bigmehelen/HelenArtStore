package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.exceptions.InvalidCredentialsException;
import com.helenartstore.HelenArtStore.exceptions.UserAlreadyExistException;
import com.helenartstore.HelenArtStore.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistException("user with email already exist");
        }
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserAlreadyExistException("user with username already exist");
        }
        User user = userMapper.toEntity(request);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(
                jwt,
                "Bearer",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }


}
