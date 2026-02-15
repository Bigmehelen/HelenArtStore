package com.helenartstore.HelenArtStore.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtworkResponse {

    private Long id;
    private String name;
    private String description;
    private int quantity;
    private List<String> imageUrls;
    private BigDecimal price;
    private boolean isAvailable;
    private Long artistId;
    private String artistName;

}
