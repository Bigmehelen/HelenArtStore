package com.helenartstore.HelenArtStore.data.models;

public enum CartStatus {
    CREATED("Cart Created, awaiting payment"),
    CONFIRMED("Cart Confirmed to be in use"),
    ACTIVE("Cart is actively in use"),
    COMPLETED("cart is completed"),
    CANCELLED("Cart cancelled"),
    EXPIRED("Cart expired"),
    REFUNDED("Payment refunded");

    private final String description;

    CartStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canActivate() {
        return this == CONFIRMED;
    }

    public boolean canCancel() {
        return this == CREATED || this == CONFIRMED || this == ACTIVE;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED ||
                this == EXPIRED || this == REFUNDED;
    }

}
