package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Artworks;
import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.ArtworksRepository;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import com.helenartstore.HelenArtStore.exceptions.ArtistNotFoundException;
import com.helenartstore.HelenArtStore.exceptions.UnauthorizedArtworkCreationException;
import com.helenartstore.HelenArtStore.utils.ArtworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private ArtworksRepository artworksRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ArtworkMapper artworkMapper;

    @Override
    public ArtworkResponse createArtwork(ArtworkRequest request) {

        findAndValidateArtist(request);
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : request.getImagesUrls()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            imageUrls.add(imageUrl);
        }
        Artworks artwork = artworkMapper.toEntity(request);
        artwork.setRole(Role.ARTIST);
        Artworks savedArtwork = artworksRepository.save(artwork);
        return artworkMapper.toResponse(savedArtwork);
    }

    @Override
    public ArtworkResponse updateArtwork(@org.springframework.lang.NonNull Long id, UpdateArtwork update) {
        Artworks artwork = findArtworkById(id);
        updateArtworkFields(artwork, update);
        updateArtworkImages(artwork, update.getImagesUrls());
        @SuppressWarnings("null")
        Artworks updatedArtwork = artworksRepository.save(artwork);
        return artworkMapper.toResponse(updatedArtwork);
    }

    @Override
    public void deleteArtwork(@org.springframework.lang.NonNull Long id) {
        if (!artworksRepository.existsById(id)) {
            throw new RuntimeException("Artwork with id " + id + " not found");
        }
        artworksRepository.deleteById(id);
    }

    @Override
    public List<ArtworkResponse> getAllArtworks() {
        return artworksRepository.findAll().stream()
                .map(artworkMapper::toResponse)
                .toList();
    }

    @Override
    public List<ArtworkResponse> getArtworksByName(@org.springframework.lang.NonNull String name) {
        return artworksRepository.findByName(name).stream()
                .map(artworkMapper::toResponse)
                .toList();
    }

    private void findAndValidateArtist(ArtworkRequest request) {
        @SuppressWarnings("null")
        User artist = userRepository.findById(request.getArtistId())
                .orElseThrow(
                        () -> new ArtistNotFoundException("Artist with id " + request.getArtistId() + " not found"));

        if (artist.getRole() != Role.ARTIST) {
            throw new UnauthorizedArtworkCreationException("Artist not found");
        }
    }

    private Artworks findArtworkById(@org.springframework.lang.NonNull Long id) {
        return artworksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork with id " + id + " not found"));
    }

    private void updateArtworkFields(Artworks artwork, UpdateArtwork update) {
        if (update.getName() != null)
            artwork.setName(update.getName());
        if (update.getDescription() != null)
            artwork.setDescription(update.getDescription());
        if (update.getQuantity() != null && update.getQuantity() > 0)
            artwork.setQuantity(update.getQuantity());
        if (update.getPrice() != null)
            artwork.setPrice(update.getPrice());
        if (update.getAvailable() != null)
            artwork.setAvailable(update.getAvailable());
    }

    private void updateArtworkImages(Artworks artwork, List<MultipartFile> images) {
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = images.stream()
                    .map(cloudinaryService::uploadImage)
                    .toList();
            artwork.setImagesUrls(imageUrls);
        }
    }
}
