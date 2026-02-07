package com.helenartstore.HelenArtStore.data.repository;

import java.math.BigDecimal;

public interface AvailableArtworksProjection {
    Long getId();
    String getName();
    String getDescription();
    Integer getQuantity();
    Integer getAvailableArtworks();
    BigDecimal getPrice();
}
