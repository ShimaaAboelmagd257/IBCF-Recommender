package com.example.recs.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ratings")
@Entity
public class Rating {

    @EmbeddedId
    private RatingId id;

    private double rating;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class RatingId implements Serializable {
        private Long userId;
        private Long movieId;
    }
}

