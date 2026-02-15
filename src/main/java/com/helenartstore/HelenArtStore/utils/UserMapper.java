package com.helenartstore.HelenArtStore.utils;

import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(RegisterRequest request) {
        return modelMapper.map(request, User.class);
    }
}
