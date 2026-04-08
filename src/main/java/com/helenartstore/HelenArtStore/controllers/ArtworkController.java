
package com.helenartstore.HelenArtStore.controllers;

import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.dtos.request.ArtworkRequest;
import com.helenartstore.HelenArtStore.dtos.request.UpdateArtwork;
import com.helenartstore.HelenArtStore.dtos.response.ArtworkResponse;
import com.helenartstore.HelenArtStore.services.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @PostMapping(consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<ArtworkResponse> createArtwork(
            @AuthenticationPrincipal User artist,
            @ModelAttribute ArtworkRequest request) {
        ArtworkResponse response = artworkService.createArtwork(artist, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<ArtworkResponse> updateArtwork(@AuthenticationPrincipal User artist,
            @PathVariable @org.springframework.lang.NonNull Long id,
            @ModelAttribute UpdateArtwork update) {
        ArtworkResponse response = artworkService.updateArtwork(artist, id, update);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Void> deleteArtwork(@AuthenticationPrincipal User artist,
            @PathVariable @org.springframework.lang.NonNull Long id) {
        artworkService.deleteArtwork(artist, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ArtworkResponse>> getAllArtworks() {
        List<ArtworkResponse> responses = artworkService.getAllArtworks();
        // return new ResponseEntity<>(responses, HttpStatus.OK);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtworkResponse>> getArtworksByName(
            @RequestParam @org.springframework.lang.NonNull String name) {
        List<ArtworkResponse> responses = artworkService.getArtworksByName(name);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
