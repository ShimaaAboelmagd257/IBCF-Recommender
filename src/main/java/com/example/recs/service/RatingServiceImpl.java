package com.example.recs.service;

import com.example.recs.domain.dto.MovieDto;
import com.example.recs.domain.dto.RatingDto;
import com.example.recs.domain.entity.MovieSimilarity;
import com.example.recs.domain.entity.Rating;
import com.example.recs.mappers.RatingsMapperImpl;
import com.example.recs.repository.MovieSimRepository;
import com.example.recs.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService{

    private final RatingRepository ratingRepository;
    private final RatingsMapperImpl mapper;
    private final IBCFServiceImpl ibcfService;
    private final TmdbService tmdbService;
    private static final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);

    @Transactional
    @Override
    public RatingDto saveRating(RatingDto ratingDto) {
        log.info("Saving rating: user={}, movie={}, rating={}",
                ratingDto.getUserId(), ratingDto.getMovieId(), ratingDto.getRating());
        if (!tmdbService.exists(ratingDto.getMovieId())) {
            throw new IllegalArgumentException("TMDB movie does not exist !!!");
        }
        Rating rating = new Rating();
        rating.setId(new Rating.RatingId(ratingDto.getUserId(),ratingDto.getMovieId()));
        rating.setRating(ratingDto.getRating());
        Rating savedRating = ratingRepository.save(rating);
        ibcfService.computeSim(rating.getId().getMovieId());
        log.info("Saving rating: user={}, movie={}, rating={}",
                ratingDto.getUserId(), ratingDto.getMovieId(), ratingDto.getRating());
        return mapper.toDto(savedRating);
    }

    @Override
    public List<RatingDto> getRatingsByUser(Long userId) {
        return ratingRepository.findByIdUserId(userId).stream().map(mapper::toDto).collect(Collectors.toList());
    }
    public List<Rating> getAllRatings(){
        return ratingRepository.findAll();
    }

}
