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
    private Long id;
    private String name;
    private String description;
    private int quantity;
    @ElementCollection
    private List<String> imagesUrls;
    private BigDecimal price;
    private boolean isAvailable;
    @Enumerated(EnumType.STRING)
    private Role role;

}
