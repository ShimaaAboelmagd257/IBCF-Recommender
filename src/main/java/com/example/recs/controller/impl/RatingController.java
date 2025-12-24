package com.example.recs.controller.impl;


import com.example.recs.domain.dto.RatingDto;
import com.example.recs.domain.entity.Rating;
import com.example.recs.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    @PostMapping
    public ResponseEntity<RatingDto> submitRating(@RequestBody RatingDto ratingDto){
        return ResponseEntity.ok(ratingService.saveRating(ratingDto));

    }
    @PostMapping("/submit-all")
    public ResponseEntity<List<RatingDto>> submitRatings(@RequestBody List<RatingDto> ratingDto){
        return ResponseEntity.ok(ratingService.saveRatings(ratingDto));

    }
    @GetMapping
    public ResponseEntity<List<Rating>> getALLRatings(){
        return ResponseEntity.ok(ratingService.getAllRatings());

    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingDto>> getRatingsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(ratingService.getRatingsByUser(userId));
    }
}
