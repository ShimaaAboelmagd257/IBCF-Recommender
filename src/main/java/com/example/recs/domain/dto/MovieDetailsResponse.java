package com.example.recs.domain.dto;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MovieDetailsResponse {

    private boolean adult;
    private String backdrop_path;
    private CollectionResponse belongs_to_collection;
    private int budget;
    private List<GenreDto> genres;
    private String homepage;
    private int id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private List<ProductionCompanyDto> production_companies;
    private List<ProductionCountryDto> production_countries;
    private String release_date;
    private long revenue;
    private int runtime;
    private List<SpokenLanguageDto> spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;

    @Data
    public static class GenreDto {
        private int id;
        private String name;
    }

    @Data
    public static class ProductionCompanyDto {
        private int id;
        private String logo_path;
        private String name;
        private String origin_country;
    }

    @Data
    public static class ProductionCountryDto {
        private String iso_3166_1;
        private String name;
    }

    @Data
    public static class SpokenLanguageDto {
        private String english_name;
        private String iso_639_1;
        private String name;
    }

    @Data
    public static class CollectionResponse {
        private int id;
        private String name;
        private String poster_path;
        private String backdrop_path;
    }
}
