package com.example.recs.service;

import com.example.recs.domain.dto.RatingDto;

import java.util.List;

public interface RatingService {
    RatingDto saveRating(RatingDto ratingDto);
    List<RatingDto> getRatingsByUser(Long userId);
}
