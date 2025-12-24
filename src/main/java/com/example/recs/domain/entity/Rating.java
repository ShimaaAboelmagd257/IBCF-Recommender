package com.example.recs.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ratings")
@Entity
public class Rating {

    @EmbeddedId
    private RatingId id;

    private Double rating;
   // private Instant created = Instant.now();

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class RatingId implements Serializable {
        private Long userId;
        private Integer movieId;
    }
}

