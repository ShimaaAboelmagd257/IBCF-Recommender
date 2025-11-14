package com.example.recs.domain.dto;


import lombok.Data;

import java.util.List;

@Data
public class TmdbGenreResponse {

    private List<Genre> genres;

    @Data
    public static class Genre{
        private int id;
        private String name;
    }
}
