package com.example.recs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TmdbMovieDto {
    private String poster_path;
    private String overview;
    private String release_date;
    private String id;
    private String title;
    private Double vote_average;
}
