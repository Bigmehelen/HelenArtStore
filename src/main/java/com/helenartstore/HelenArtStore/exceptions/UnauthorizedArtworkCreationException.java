package com.helenartstore.HelenArtStore.exceptions;

public class UnauthorizedArtworkCreationException extends RuntimeException {
    public UnauthorizedArtworkCreationException(String message) {
        super(message);
    }

    public UnauthorizedArtworkCreationException(Long userId) {
        super("User with ID " + userId + " is not authorized to create artworks. Only artists can create artworks.");
    }
}
