package com.helenartstore.HelenArtStore.dtos.request;

import com.helenartstore.HelenArtStore.data.models.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArtworkRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Quantity is required")
    private int quantity;
    @NotBlank(message = "Image is required")
    private String imageUrls;
    @NotBlank(message = "price is required")
    private BigDecimal price;
    private boolean isAvailable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private User Artist;
}
