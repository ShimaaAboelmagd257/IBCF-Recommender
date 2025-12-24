package com.example.recs.service;

import com.example.recs.domain.dto.RatingDto;
import com.example.recs.domain.entity.Rating;

import java.util.List;

public interface RatingService {
    RatingDto saveRating(RatingDto ratingDto);
    List<RatingDto> getRatingsByUser(Long userId);
    List<Rating> getAllRatings();
    List<RatingDto> saveRatings(List<RatingDto> ratingDtos);
}
