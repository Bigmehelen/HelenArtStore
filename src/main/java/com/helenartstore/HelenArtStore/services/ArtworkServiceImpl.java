package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;

import java.util.List;

public class ArtworkServiceImpl implements ArtworkService {
    @Override
    public ArtworkResponse createArtwork(ArtworkRequest request) {
        return null;
    }

    @Override
    public ArtworkResponse updateArtwork(Long id, UpdateArtwork update) {
        return null;
    }

    @Override
    public void deleteArtwork(Long id) {

    }

    @Override
    public List<ArtworkResponse> getAllArtworks() {
        return List.of();
    }

    @Override
    public List<ArtworkResponse> getAllArtworksByName(String name) {
        return List.of();
    }
}
