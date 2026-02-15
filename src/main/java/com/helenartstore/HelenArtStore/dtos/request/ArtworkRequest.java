package com.helenartstore.HelenArtStore.dtos.request;

import com.helenartstore.HelenArtStore.data.models.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtworkRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Quantity is required")
    private int quantity;
    @NotBlank(message = "Image is required")
    private List<MultipartFile> images;
    @NotBlank(message = "price is required")
    private BigDecimal price;
    private boolean isAvailable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private User Artist;
}
