package com.example.recs.controller.impl;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.dto.MovieDetailsResponse;
import com.example.recs.domain.dto.TmdbGenreResponse;
import com.example.recs.domain.dto.TmdbMoviesResponse;
import com.example.recs.service.TMDbClient;
import com.example.recs.service.TmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final TmdbService tmdbService;
    
    @GetMapping("/popular")
    public ResponseEntity<TmdbMoviesResponse> getPopularMovies(@RequestParam(required = false) String genre){
        return ResponseEntity.ok(tmdbService.getPopularMovies());
    }
    @GetMapping("/genres")
    public ResponseEntity<TmdbGenreResponse> getGenres(){
        return ResponseEntity.ok(tmdbService.getGenres());
    }
    @GetMapping("/genres/{genreId}")
    public ResponseEntity<TmdbMoviesResponse> getMoviesByGenre(@PathVariable int genreId){
        return ResponseEntity.ok(tmdbService.getMoviesByGenre(genreId));
    }
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDetailsResponse> getMovieDetails(@PathVariable int movieId){
        return ResponseEntity.ok(tmdbService.getMovieDetails(movieId));
    }



}
