package com.helenartstore.HelenArtStore.services;

import com.helenartstore.HelenArtStore.data.repository.ArtworksRepository;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ArtworkServiceImplTest {
    ArtworkRequest artworkRequest;
    ArtworkResponse artworkResponse;

    @Mock
    private ArtworksRepository artworksRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ArtworkServiceImpl artworkService;

    @BeforeEach
    void setUp() {
        artworkRequest = new ArtworkRequest();
        artworkRequest.setName("Moremi");
        artworkRequest.setDescription("Artwork set");
        artworkRequest.setQuantity(3);
        artworkRequest.setAvailable(true);
        artworkRequest.setPrice(BigDecimal.valueOf(4000));
        artworkRequest.setImageUrls("imageUrls");

        artworkResponse = new ArtworkResponse();
        artworkResponse.setId(1L);
        artworkResponse.setName("Moremi");
        artworkResponse.setDescription("Artwork set");
        artworkResponse.setQuantity(3);
        artworkResponse.setAvailable(true);
        artworkResponse.setPrice(BigDecimal.valueOf(4000));
        artworkResponse.setImageUrls("imageUrls");
    }

    @Test
    void testThatCanCreateArtwork() {

    }

}