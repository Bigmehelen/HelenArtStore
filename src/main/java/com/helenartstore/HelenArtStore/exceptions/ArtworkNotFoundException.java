package com.helenartstore.HelenArtStore.exceptions;

public class ArtworkNotFoundException extends RuntimeException {
    public ArtworkNotFoundException(String message) {
        super(message);
    }
}
