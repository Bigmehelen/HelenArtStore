package com.helenartstore.HelenArtStore.utils;

import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.dtos.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User map(RegisterRequest request);
    RegisterRequest toDto(User user);
}
