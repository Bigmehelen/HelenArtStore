package com.helenartstore.HelenArtStore.dtos.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateArtwork {
    private String name;
    private String description;
    private Integer quantity;
    private List<MultipartFile> imagesUrls;
    private BigDecimal price;
    private Boolean available;
}
