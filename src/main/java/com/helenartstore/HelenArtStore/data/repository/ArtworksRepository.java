package com.helenartstore.HelenArtStore.data.repository;

import com.helenartstore.HelenArtStore.data.models.Artworks;
import com.helenartstore.HelenArtStore.dtos.response.AvailableArtworksProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArtworksRepository extends JpaRepository <Artworks, Long>{
    List<Artworks> findByArtistId(Long artistId);

    @Query(value = """
        SELECT
            p.id AS id,
            p.name AS name,
            p.description As description
            p.quantity AS quantity,
            (p.quantity - COALESCE(COUNT(r.id), 0)) AS availableArtworks,
            p.price As price
        FROM Artworks p
        LEFT JOIN cart r
            ON p.id = r.artworks_id
            AND r.status IN ('CONFIRMED', 'ACTIVE')
            AND :startTime < r.end_time
            AND :endTime > r.start_time
        WHERE p.is_active = true
        GROUP BY\s
            p.id,
            p.name,
            p.description,
            p.quantity,
            p.price
        HAVING (p.quantity - COALESCE(COUNT(r.id), 0)) > 0 
        """, nativeQuery = true)
    List<AvailableArtworksProjection> searchAvailableArtworks(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    Optional<Artworks> findByName(String name);

}
