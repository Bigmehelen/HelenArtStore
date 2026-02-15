package com.helenartstore.HelenArtStore.dtos.response;

import com.helenartstore.HelenArtStore.data.models.User;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtworkResponse {

    private Long id;
    private String name;
    private String description;
    private int quantity;
    private List<MultipartFile> images;
    private BigDecimal price;
    private boolean isAvailable;
    private User Artist;

}
