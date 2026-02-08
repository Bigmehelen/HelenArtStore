package com.helenartstore.HelenArtStore.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String userNameNotFound) {
        super(userNameNotFound);
    }
}
