package com.example.recs.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "movies_similarity",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"movieX", "movieY"})
        })
@Entity

public class MovieSimilarity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long primaryKey;
    private Integer movieX;
    private Integer movieY;
    private Double sim;
}
