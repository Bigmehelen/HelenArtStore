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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworksRepository artworksRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ArtworkMapper artworkMapper;

    @Override
    public ArtworkResponse createArtwork(ArtworkRequest request) {

        User artist = findAndValidateArtist(request.getArtistId());

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : request.getImagesUrls()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            imageUrls.add(imageUrl);
        }
        Artworks artwork = artworkMapper.toEntity(request);
        artwork.setArtist(artist);
        Artworks savedArtwork = artworksRepository.save(artwork);
        return artworkMapper.toResponse(savedArtwork);
    }

    @Override
    public ArtworkResponse updateArtwork(Long id, UpdateArtwork update) {
        Artworks artwork = artworksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork with id " + id + " not found"));

        // TODO: Validate that the current user owns this artwork

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

        if (update.getImagesUrls() != null && !update.getImagesUrls().isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : update.getImagesUrls()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            artwork.setImagesUrls(imageUrls);
        }

        Artworks updatedArtwork = artworksRepository.save(artwork);
        return artworkMapper.toResponse(updatedArtwork);
    }

    @Override
    public void deleteArtwork(Long id) {
        if (!artworksRepository.existsById(id)) {
            throw new RuntimeException("Artwork with id " + id + " not found");
        }
        artworksRepository.deleteById(id);
    }

    @Override
    public List<ArtworkResponse> getAllArtworks() {
        return artworksRepository.findAll().stream()
                .map(artwork -> artworkMapper.toResponse(artwork))
                .toList();
    }

    @Override
    public List<ArtworkResponse> getArtworksByName(String name) {
        return artworksRepository.findByName(name).stream()
                .map(artwork -> artworkMapper.toResponse(artwork))
                .toList();
    }

    private User findAndValidateArtist(Long artistId) {
        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException(artistId));

        if (artist.getRole() != Role.ARTIST) {
            throw new UnauthorizedArtworkCreationException(artist.getId());
        }

        return artist;
    }

}
