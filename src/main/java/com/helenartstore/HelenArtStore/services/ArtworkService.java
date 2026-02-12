package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtworkService {
    ArtworkResponse createArtwork (ArtworkRequest request);
    ArtworkResponse updateArtwork(Long id, UpdateArtwork update);
    void deleteArtwork(Long id);
    List<ArtworkResponse> getAllArtworks();
    List<ArtworkResponse> getAllArtworksByName(String name);
}
