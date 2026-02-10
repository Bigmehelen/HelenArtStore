package com.helenartstore.HelenArtStore.dtos.response;

import com.helenartstore.HelenArtStore.data.models.Role;

public record AuthResponse(
        String token,
        String type,
        Long id,
        String username,
        String email,
        Role role
) {
    public AuthResponse(String token, Long id, String username, String email, Role role) {
        this(token, "Bearer", id, username, email, role);
    }
}
