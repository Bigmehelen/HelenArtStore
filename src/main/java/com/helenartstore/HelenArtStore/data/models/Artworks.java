package com.helenartstore.HelenArtStore.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "artworks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Artworks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.lang.Nullable
    private Long id;
    private String name;
    private String description;
    private int quantity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "artwork_images", joinColumns = @JoinColumn(name = "artwork_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    private BigDecimal price;
    @Column(name = "is_available", nullable = false)
    private boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    private User artist;

}
