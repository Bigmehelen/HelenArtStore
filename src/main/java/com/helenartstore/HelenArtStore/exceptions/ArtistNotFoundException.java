package com.helenartstore.HelenArtStore.exceptions;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(String message) {
        super(message);
    }

    public ArtistNotFoundException(Long artistId) {
        super("Artist with ID " + artistId + " not found");
    }
}
