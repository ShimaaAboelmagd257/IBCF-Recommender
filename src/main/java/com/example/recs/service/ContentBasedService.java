package com.example.recs.service;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.dto.MovieDto;
import com.example.recs.domain.dto.TmdbMoviesResponse;
import com.example.recs.mappers.MovieMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentBasedService {

    private final TmdbService tmdbService;


    public List<Movie> recommendForNewUsersByGenres(List<Integer> genresId, int limit ){
        List<Movie> movieRecs = new ArrayList<>();
        for (Integer genreId: genresId){
            TmdbMoviesResponse moviesResponse = tmdbService.getMoviesByGenre(genreId);
            List<Movie> results = moviesResponse.getResults();
            Collections.shuffle(results);

            List<Movie> limitedMovies =
                    results.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
            movieRecs.addAll(limitedMovies);

        }

        return movieRecs;
    }

}
