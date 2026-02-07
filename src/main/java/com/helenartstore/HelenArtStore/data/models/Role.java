package com.helenartstore.HelenArtStore.data.models;

public enum Role {
    USER,
    ADMIN,
    ARTIST;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
