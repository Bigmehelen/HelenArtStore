package com.helenartstore.HelenArtStore.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        String username,
        @NotBlank(message = "Password is required")
        String password
) {}
