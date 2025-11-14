package com.example.recs.controller.impl;


import com.example.recs.domain.dto.Movie;
import com.example.recs.service.IBCFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final IBCFService ibcfService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Movie>> getRecommendationsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit
    ){
        List<Movie> recommendation = ibcfService.recommendationForUser(userId);
        return ResponseEntity.ok(recommendation);
    }
}
