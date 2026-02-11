package com.helenartstore.HelenArtStore.dtos.response;

import com.helenartstore.HelenArtStore.data.models.Role;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private Role role;

    public AuthResponse(String token, String type, Long id, String username, String email, Role role) {
        this.type = type;
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
