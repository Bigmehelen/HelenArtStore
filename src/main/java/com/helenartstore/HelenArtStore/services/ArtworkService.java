package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtworkService {
    ArtworkResponse createArtwork(@org.springframework.lang.NonNull Long id, ArtworkRequest request);

    ArtworkResponse updateArtwork(@org.springframework.lang.NonNull Long id, UpdateArtwork update);

    void deleteArtwork(@org.springframework.lang.NonNull Long id);

    List<ArtworkResponse> getAllArtworks();

    List<ArtworkResponse> getArtworksByName(@org.springframework.lang.NonNull String name);
}
