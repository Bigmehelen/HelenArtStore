package com.helenartstore.HelenArtStore.controllers;

import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import com.helenartstore.HelenArtStore.services.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<ArtworkResponse> createArtwork(@ModelAttribute ArtworkRequest request) {
        ArtworkResponse response = artworkService.createArtwork(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<ArtworkResponse> updateArtwork(@PathVariable @org.springframework.lang.NonNull Long id,
            @ModelAttribute UpdateArtwork update) {
        ArtworkResponse response = artworkService.updateArtwork(id, update);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable @org.springframework.lang.NonNull Long id) {
        artworkService.deleteArtwork(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtworkResponse>> getAllArtworks() {
        List<ArtworkResponse> responses = artworkService.getAllArtworks();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtworkResponse>> getArtworksByName(
            @RequestParam @org.springframework.lang.NonNull String name) {
        List<ArtworkResponse> responses = artworkService.getArtworksByName(name);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
