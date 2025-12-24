package com.example.recs.controller.impl;

import com.example.recs.domain.entity.MovieSimilarity;
import com.example.recs.repository.MovieSimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sims")
@RequiredArgsConstructor
public class SimsMovieController {

    private final MovieSimRepository movieSimRepository;

    @GetMapping
    public ResponseEntity<List<MovieSimilarity>> getMovieSim(){
        return ResponseEntity.ok(movieSimRepository.findAll());
    }
}
