package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtworkService {
    ArtworkResponse createArtwork(com.helenartstore.HelenArtStore.data.models.User artist, ArtworkRequest request);

    ArtworkResponse updateArtwork(com.helenartstore.HelenArtStore.data.models.User artist,
            @org.springframework.lang.NonNull Long id, UpdateArtwork update);

    void deleteArtwork(com.helenartstore.HelenArtStore.data.models.User artist,
            @org.springframework.lang.NonNull Long id);

    List<ArtworkResponse> getAllArtworks();
    ArtworkResponse getArtworkById(@org.springframework.lang.NonNull Long id);

    List<ArtworkResponse> getArtworksByName(@org.springframework.lang.NonNull String name);
}
