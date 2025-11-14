package com.example.recs.service;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.dto.MovieDto;

import java.util.List;

public interface IBCFService {
    List<Movie> recommendationForUser(Long userId);
}
