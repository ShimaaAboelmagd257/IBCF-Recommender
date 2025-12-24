package com.example.recs.domain.dto;

import java.util.Map;

public record MovieRatingDto(
        Integer movieId,
        Map<Long, Double> userRatings
) {}