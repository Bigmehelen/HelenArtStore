package com.helenartstore.HelenArtStore.controllers;

import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import com.helenartstore.HelenArtStore.services.ArtworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArtworkControllerTest {

    @Mock
    private ArtworkService artworkService;

    @InjectMocks
    private ArtworkController artworkController;

    private MockMvc mockMvc;

    private ArtworkResponse artworkResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(artworkController).build();

        artworkResponse = new ArtworkResponse();
        artworkResponse.setId(1L);
        artworkResponse.setName("Moremi");
        artworkResponse.setDescription("Artwork set");
        artworkResponse.setQuantity(3);
        artworkResponse.setPrice(BigDecimal.valueOf(4000));
        artworkResponse.setAvailable(true);
        artworkResponse.setImageUrls(List.of("url1", "url2"));
        artworkResponse.setArtistId(1L);
        artworkResponse.setArtistName("artist_user");
    }

    @Test
    void createArtwork() throws Exception {
        MockMultipartFile image = new MockMultipartFile("imagesUrls", "test.jpg", "image/jpeg",
                "test image content".getBytes());

        when(artworkService.createArtwork(any(ArtworkRequest.class))).thenReturn(artworkResponse);

        mockMvc.perform(multipart("/api/v1/artworks")
                .file(image)
                .param("name", "Moremi")
                .param("description", "Artwork set")
                .param("quantity", "3")
                .param("price", "4000")
                .param("artistId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Moremi"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateArtwork() throws Exception {
        MockMultipartFile image = new MockMultipartFile("imagesUrls", "test.jpg", "image/jpeg",
                "test image content".getBytes());

        ArtworkResponse updatedResponse = new ArtworkResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Moremi Updated");

        when(artworkService.updateArtwork(eq(1L), any(UpdateArtwork.class))).thenReturn(updatedResponse);

        mockMvc.perform(multipart("/api/v1/artworks/{id}", 1L)
                        .file(image)
                .param("name", "Moremi Updated")
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Moremi Updated"));
    }

    @Test
    void deleteArtwork() throws Exception {
        doNothing().when(artworkService).deleteArtwork(1L);

        mockMvc.perform(delete("/api/v1/artworks/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllArtworks() throws Exception {
        when(artworkService.getAllArtworks()).thenReturn(List.of(artworkResponse));

        mockMvc.perform(get("/api/v1/artworks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Moremi"));
    }

    @Test
    void getArtworksByName() throws Exception {
        when(artworkService.getArtworksByName("Moremi")).thenReturn(List.of(artworkResponse));

        mockMvc.perform(get("/api/v1/artworks/search")
                .param("name", "Moremi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Moremi"));
    }
}
