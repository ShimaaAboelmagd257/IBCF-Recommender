package com.example.recs.service;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.dto.MovieDetailsResponse;
import com.example.recs.domain.dto.TmdbGenreResponse;
import com.example.recs.domain.dto.TmdbMoviesResponse;

public interface TmdbService {
    TmdbMoviesResponse getPopularMovies();
    TmdbGenreResponse getGenres();
    TmdbMoviesResponse getMoviesByGenre(int genreId);
    MovieDetailsResponse getMovieDetails(int movieId );
    boolean exists(Integer movieId);
}
