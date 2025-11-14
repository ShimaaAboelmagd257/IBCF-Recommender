package com.example.recs.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TmdbSearchResponse {
    private int page;
    private List<Movie> results;
    private int total_pages;
    private int total_results;
}
