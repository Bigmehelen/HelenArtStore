package com.helenartstore.HelenArtStore.controllers;

import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.dtos.response.AuthResponse;
import com.helenartstore.HelenArtStore.services.AuthService;
import com.helenartstore.HelenArtStore.services.CustomUserDetailsService;
import com.helenartstore.HelenArtStore.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("null")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService; // Required because SecurityConfig might scan it or filters use it

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "testuser")
    void becomeArtist_Success() throws Exception {
        AuthResponse response = new AuthResponse();
        response.setUsername("testuser");
        response.setRole(Role.ARTIST);
        response.setToken("new_token");

        when(authService.upgradeToArtist("testuser")).thenReturn(response);

        mockMvc.perform(post("/api/user/auth/become-artist")
                .with(csrf())) // CSRF might be needed if security is active, but filters are disabled
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ARTIST"))
                .andExpect(jsonPath("$.token").value("new_token"));
    }
}
