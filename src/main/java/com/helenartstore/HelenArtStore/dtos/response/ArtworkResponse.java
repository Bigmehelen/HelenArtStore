package com.helenartstore.HelenArtStore.dtos.response;

import com.helenartstore.HelenArtStore.data.models.User;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ArtworkResponse {

    private Long id;
    private String name;
    private String description;
    private int quantity;
    private String imageUrls;
    private BigDecimal price;
    private boolean isAvailable;
    private User Artist;

}
