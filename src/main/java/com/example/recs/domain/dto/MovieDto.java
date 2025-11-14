package com.example.recs.domain.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    @Id
    private Long movieId;
    private String title;
    private String genres;
    private String tmdbId;         // from TMDb
    private String overview;
    private String posterUrl;
    private LocalDate releaseDate;
}
