package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.models.Artworks;
import java.util.stream.Collectors;
import com.helenartstore.HelenArtStore.data.models.Role;
import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.ArtworksRepository;
import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import com.helenartstore.HelenArtStore.exceptions.ArtworkNotFoundException;
import com.helenartstore.HelenArtStore.exceptions.UnauthorizedArtworkCreationException;
import com.helenartstore.HelenArtStore.utils.ArtworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private ArtworksRepository artworksRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ArtworkMapper artworkMapper;

    @Override
    public ArtworkResponse createArtwork(User artist, ArtworkRequest request) {
        if (!artist.getRoles().contains(Role.ARTIST)) {
            throw new UnauthorizedArtworkCreationException("Only artists can create artworks");
        }
        List<String> imageUrls = new ArrayList<>();
        if (request.getImagesUrls() != null) {
            for (MultipartFile image : request.getImagesUrls()) {
                if (!image.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadImage(image);
                    imageUrls.add(imageUrl);
                }
            }
        }
        Artworks artwork = artworkMapper.toEntity(request);
        artwork.setArtist(artist);
        artwork.setImagesUrls(imageUrls);
        Artworks savedArtwork = artworksRepository.save(artwork);
        return artworkMapper.toResponse(savedArtwork);
    }

    @Override
    public ArtworkResponse updateArtwork(User artist, @org.springframework.lang.NonNull Long id, UpdateArtwork update) {
        Artworks artwork = findArtworkById(id);
        if (!artwork.getArtist().getId().equals(artist.getId())) {
            throw new UnauthorizedArtworkCreationException("You can only update your own artworks");
        }
        updateArtworkFields(artwork, update);
        updateArtworkImages(artwork, update.getImagesUrls());
        Artworks updatedArtwork = artworksRepository.save(artwork);
        return artworkMapper.toResponse(updatedArtwork);
    }

    @Override
    public void deleteArtwork(User artist, @org.springframework.lang.NonNull Long id) {
        Artworks artwork = findArtworkById(id);
        if (!artwork.getArtist().getId().equals(artist.getId())) {
            throw new UnauthorizedArtworkCreationException("You can only delete your own artworks");
        }
        artworksRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public ArtworkResponse getArtworkById(@org.springframework.lang.NonNull Long id) {
        Artworks artwork = findArtworkById(id); 
        return artworkMapper.toResponse(artwork);
    }

    private Artworks findArtworkById(@org.springframework.lang.NonNull Long id) {
        return artworksRepository.findById(id)
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork with id " + id + " not found"));
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

    public void updateArtworkImages(Artworks artwork, List<MultipartFile> images) {
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = images.stream()
                    .filter(file -> !file.isEmpty())
                    .map(cloudinaryService::uploadImage)
                    .collect(Collectors.toList());
            if (!imageUrls.isEmpty()) {
                artwork.setImagesUrls(imageUrls);
            }
        }
    }
}
