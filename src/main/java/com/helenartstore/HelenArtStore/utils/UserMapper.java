package com.helenartstore.HelenArtStore.utils;

import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.dtos.request.LoginRequest;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(RegisterRequest request) {
        return modelMapper.map(request, User.class);
    }

    public User mapToEntity(LoginRequest request) {
        return modelMapper.map(request, User.class);
    }
}
