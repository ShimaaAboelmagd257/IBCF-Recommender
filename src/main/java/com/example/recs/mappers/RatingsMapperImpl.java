package com.example.recs.mappers;

import com.example.recs.domain.dto.RatingDto;
import com.example.recs.domain.entity.Rating;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RatingsMapperImpl implements Mapper<Rating, RatingDto> {

    private final ModelMapper mapper;

    public RatingsMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public RatingDto toDto(Rating rating) {
        return mapper.map(rating, RatingDto.class);
    }

    @Override
    public Rating fromDto(RatingDto ratingDto) {
        return mapper.map(ratingDto, Rating.class);
    }
}
