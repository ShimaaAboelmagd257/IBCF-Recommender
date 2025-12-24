package com.example.recs.domain.dto;

public record RecsResponse (
        Long movieId,
        String title,
        double score
){}
