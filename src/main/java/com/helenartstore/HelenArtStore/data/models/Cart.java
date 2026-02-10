package com.helenartstore.HelenArtStore.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart", indexes = {
        @Index(name = "idx_cart_code", columnList = "cartCode"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_artworks_id", columnList = "artworks_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String cartCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Artworks artworks;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private LocalDateTime actualExitTime;
    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // @Builder.Default
    // private CartStatus status = CartStatus.CREATED;
    // @Column(nullable = false)
    // private Double baseAmount;
    // @Builder.Default
    // private Double extraAmount = 0.0;
    // @Column(nullable = false)
    // private Double totalAmount;
    // @CreationTimestamp
    // @Column(name = "created_at", nullable = false, updatable = false)
    // private LocalDateTime createdAt;
    // @Version
    // private Long version;

}