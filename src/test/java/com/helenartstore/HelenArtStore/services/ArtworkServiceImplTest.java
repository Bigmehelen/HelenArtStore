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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ArtworkServiceImplTest {

    private ArtworkRequest artworkRequest;
    private User artistUser;
    private User regularUser;
    private Artworks savedArtwork;

    @Mock
    private ArtworksRepository artworksRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ArtworkMapper artworkMapper;

    @InjectMocks
    private ArtworkServiceImpl artworkService;

    @BeforeEach
    void setUp() {
        artistUser = User.builder()
                .id(1L)
                .username("artist_user")
                .email("artist@example.com")
                .role(Role.ARTIST)
                .build();

        regularUser = User.builder()
                .id(2L)
                .username("regular_user")
                .email("user@example.com")
                .role(Role.USER)
                .build();

        artworkRequest = new ArtworkRequest();
        artworkRequest.setName("Moremi");
        artworkRequest.setDescription("Artwork set");
        artworkRequest.setQuantity(3);
        artworkRequest.setAvailable(true);
        artworkRequest.setPrice(BigDecimal.valueOf(4000));
        artworkRequest.setArtistId(1L);

        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                "image1.jpg",
                "image/jpeg",
                "dummy image content".getBytes());

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                "image2.jpg",
                "image/jpeg",
                "dummy image content".getBytes());
        List<MultipartFile> images = List.of(image1, image2);
        artworkRequest.setImagesUrls(images);

        savedArtwork = new Artworks();
        savedArtwork.setId(1L);
        savedArtwork.setName("Moremi");
        savedArtwork.setDescription("Artwork set");
        savedArtwork.setQuantity(3);
        savedArtwork.setAvailable(true);
        savedArtwork.setPrice(BigDecimal.valueOf(4000));
        savedArtwork.setImagesUrls(List.of("url1", "url2"));
        savedArtwork.setArtist(artistUser);

        ArtworkResponse response = new ArtworkResponse();
        response.setId(1L);
        response.setName("Moremi");
        response.setDescription("Artwork set");
        response.setQuantity(3);
        response.setPrice(BigDecimal.valueOf(4000));
        response.setAvailable(true);
        response.setImageUrls(List.of("url1", "url2"));
        response.setArtistId(1L);
        response.setArtistName("artist_user");
    }

    @Test
    void testThatCanCreateArtwork() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(artistUser));
        when(cloudinaryService.uploadImage(any(MultipartFile.class)))
                .thenReturn("url1")
                .thenReturn("url2");
        when(artworkMapper.toEntity(any(ArtworkRequest.class))).thenReturn(savedArtwork);
        when(artworksRepository.save(any(Artworks.class))).thenReturn(savedArtwork);
        when(artworkMapper.toResponse(any(Artworks.class))).thenReturn(new ArtworkResponse() {
            {
                setId(1L);
                setName("Moremi");
                setDescription("Artwork set");
                setQuantity(3);
                setPrice(BigDecimal.valueOf(4000));
                setAvailable(true);
                setImageUrls(List.of("url1", "url2"));
                setArtistId(1L);
                setArtistName("artist_user");
            }
        });

        ArtworkResponse response = artworkService.createArtwork(artworkRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Moremi", response.getName());
        assertEquals("Artwork set", response.getDescription());
        assertEquals(3, response.getQuantity());
        assertEquals(BigDecimal.valueOf(4000), response.getPrice());
        assertTrue(response.isAvailable());
        assertEquals(2, response.getImageUrls().size());
        assertEquals("url1", response.getImageUrls().get(0));
        assertEquals("url2", response.getImageUrls().get(1));
        assertEquals(1L, response.getArtistId());
        assertEquals("artist_user", response.getArtistName());


        verify(userRepository, times(1)).findById(1L);
        verify(cloudinaryService, times(2)).uploadImage(any(MultipartFile.class));
        verify(artworkMapper, times(1)).toEntity(any(ArtworkRequest.class));
        verify(artworksRepository, times(1)).save(any(Artworks.class));
        verify(artworkMapper, times(1)).toResponse(any(Artworks.class));
    }

    @Test
    void testThatNonArtistCannotCreateArtwork() {
        artworkRequest.setArtistId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));


        UnauthorizedArtworkCreationException exception = assertThrows(
                UnauthorizedArtworkCreationException.class,
                () -> artworkService.createArtwork(artworkRequest));

        assertTrue(exception.getMessage().contains("not authorized to create artworks"));
        assertTrue(exception.getMessage().contains("Only artists can create artworks"));

        verify(cloudinaryService, never()).uploadImage(any(MultipartFile.class));
        verify(artworksRepository, never()).save(any(Artworks.class));
    }

    @Test
    void testThatArtworkCreationFailsWhenArtistNotFound() {
        artworkRequest.setArtistId(999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ArtistNotFoundException exception = assertThrows(
                ArtistNotFoundException.class,
                () -> artworkService.createArtwork(artworkRequest));

        assertTrue(exception.getMessage().contains("Artist with ID 999 not found"));


        verify(cloudinaryService, never()).uploadImage(any(MultipartFile.class));
        verify(artworksRepository, never()).save(any(Artworks.class));
    }

    @Test
    void testThatCanUpdateArtwork() {
        UpdateArtwork update = new UpdateArtwork();
        update.setName("Moremi Remixed");
        update.setPrice(BigDecimal.valueOf(5000));

        when(artworksRepository.findById(1L)).thenReturn(Optional.of(savedArtwork));
        when(artworksRepository.save(any(Artworks.class))).thenReturn(savedArtwork);
        when(artworkMapper.toResponse(any(Artworks.class))).thenReturn(new ArtworkResponse() {
            {
                setId(1L);
                setName("Moremi Remixed");
                setPrice(BigDecimal.valueOf(5000));
            }
        });

        ArtworkResponse response = artworkService.updateArtwork(1L, update);
        assertNotNull(response);
        assertEquals("Moremi Remixed", response.getName());
        assertEquals(BigDecimal.valueOf(5000), response.getPrice());

        verify(artworksRepository, times(1)).save(any(Artworks.class));
    }

    @Test
    void testThatUpdateArtworkThrowsExceptionWhenNotFound() {
        UpdateArtwork update = new UpdateArtwork();
        when(artworksRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> artworkService.updateArtwork(99L, update));
    }

    @Test
    void testThatCanDeleteArtwork() {
        when(artworksRepository.existsById(1L)).thenReturn(true);
        doNothing().when(artworksRepository).deleteById(1L);

        artworkService.deleteArtwork(1L);
        verify(artworksRepository, times(1)).deleteById(1L);
    }

        @Test
        void testThatDeleteArtworkThrowsExceptionWhenNotFound() {
                when(artworksRepository.existsById(99L)).thenReturn(false);

                assertThrows(RuntimeException.class, () -> artworkService.deleteArtwork(99L));
                verify(artworksRepository, never()).deleteById(anyLong());
        }

    @Test
    void testThatCanGetAllArtworks() {
        when(artworksRepository.findAll()).thenReturn(List.of(savedArtwork));
        when(artworkMapper.toResponse(any(Artworks.class))).thenReturn(new ArtworkResponse() {
            {
                setId(1L);
                setName("Moremi");
            }
        });

        List<ArtworkResponse> responses = artworkService.getAllArtworks();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Moremi", responses.get(0).getName());
    }

    @Test
    void testThatCanGetArtworksByName() {
        when(artworksRepository.findByName("Moremi")).thenReturn(Optional.of(savedArtwork));
        when(artworkMapper.toResponse(any(Artworks.class))).thenReturn(new ArtworkResponse() {
            {
                setId(1L);
                setName("Moremi");
            }
        });

        List<ArtworkResponse> responses = artworkService.getArtworksByName("Moremi");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Moremi", responses.get(0).getName());
    }

}
