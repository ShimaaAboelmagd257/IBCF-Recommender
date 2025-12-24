package com.example.recs.service;

import com.example.recs.domain.dto.MovieDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieCache {

   private final TmdbService tmdbService;
    private final Map<Integer, MovieDetailsResponse> cache = new ConcurrentHashMap<>();
    public MovieDetailsResponse getCacheResponses(int movieId){
        return cache.computeIfAbsent(movieId ,id -> tmdbService.getMovieDetails(movieId));
    }
    public Set<Integer> getGenres(int movieId){
        MovieDetailsResponse movie = getCacheResponses(movieId);
        return movie.getGenres().stream().map(MovieDetailsResponse.GenreDto::getId)
                .collect(Collectors.toSet());
    }
    public void clearCache(){
        cache.clear();
    }
}
