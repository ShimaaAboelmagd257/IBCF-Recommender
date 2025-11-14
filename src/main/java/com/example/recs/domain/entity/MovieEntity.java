package com.example.recs.domain.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "movies")
@Entity
public class MovieEntity {
    @Id
    private Long movieId;
    private String title;
    private String genres;
    private String tmdbId;         // from TMDb
    private String overview;
    private String posterUrl;
    private LocalDate releaseDate;
}

