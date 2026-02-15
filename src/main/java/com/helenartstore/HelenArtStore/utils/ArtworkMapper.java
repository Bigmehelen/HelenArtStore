package com.helenartstore.HelenArtStore.utils;

import com.helenartstore.HelenArtStore.data.models.Artworks;
import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtworkMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Artworks toEntity(ArtworkRequest request) {
        return modelMapper.map(request, Artworks.class);
    }

    public ArtworkResponse toResponse(Artworks artwork) {
        return modelMapper.map(artwork, ArtworkResponse.class);
    }
}