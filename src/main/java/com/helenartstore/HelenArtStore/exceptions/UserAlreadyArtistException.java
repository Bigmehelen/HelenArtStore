package com.helenartstore.HelenArtStore.exceptions;

public class UserAlreadyArtistException extends RuntimeException {
    public UserAlreadyArtistException(String message) {
        super(message);
    }
}
