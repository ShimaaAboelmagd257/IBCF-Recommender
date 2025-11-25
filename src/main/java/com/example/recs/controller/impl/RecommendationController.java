package com.example.recs.controller.impl;


import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.dto.MovieDetailsResponse;
import com.example.recs.service.ContentBasedService;
import com.example.recs.service.RecommendationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final ContentBasedService contentBasedService;
    private final RecommendationsService recommendationsService;


    @GetMapping("/by_genre")
    public ResponseEntity<List<Movie>> getContentBasedByGenres(@RequestParam List<Integer> genreIds){
        List<Movie> movieRecs = contentBasedService.recommendForNewUsersByGenres(genreIds,10);
        return ResponseEntity.ok(movieRecs);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<MovieDetailsResponse>> getRecommendationsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit
    ){
        List<MovieDetailsResponse> recommendation = recommendationsService.recommendationForUser(userId,limit);
        return ResponseEntity.ok(recommendation);
    }
}
