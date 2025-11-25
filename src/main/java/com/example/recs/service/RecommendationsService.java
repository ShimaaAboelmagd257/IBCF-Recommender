package com.example.recs.service;

import com.example.recs.domain.dto.MovieDetailsResponse;
import com.example.recs.domain.entity.MovieSimilarity;
import com.example.recs.domain.entity.Rating;
import com.example.recs.mappers.RatingsMapperImpl;
import com.example.recs.repository.MovieSimRepository;
import com.example.recs.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationsService {
    private final RatingRepository ratingRepository;
    private final MovieSimRepository movieSimRepository;
    private final IBCFServiceImpl ibcfService;

    private final RatingsMapperImpl mapper;
    private final TmdbService tmdbService;
    private static final Logger log = LoggerFactory.getLogger(RecommendationsService.class);

    public List<MovieDetailsResponse> recommendationForUser(Long userId , int limit){
        log.info("Generating recommendations for user {}", userId);
        List <Rating> ratings = ratingRepository.findByIdUserId(userId);
        log.info("User {} has {} rated movies", userId, ratings.size());
        if (ratings.isEmpty()) {
            log.warn("User {} has no ratings. Returning empty recommendations.", userId);
            return List.of();
        }
        Map<Long,Double> movieUserMap =
                ratings.stream().collect(Collectors.toMap(
                        r -> r.getId().getMovieId(),Rating::getRating
                ));
        for (Long movieX : new ArrayList<>(movieUserMap.keySet())) {
            log.debug("Processing movieX {}", movieX);
            List<MovieSimilarity> sims = movieSimRepository.findByMovieX(movieX);
            if (sims == null || sims.isEmpty()) {
                ibcfService.computeSim(movieX);
            }
        }
            Map<Long,Double> scores = new HashMap<>();
        for (Long movieX: movieUserMap.keySet()) {
            List<MovieSimilarity> neighbors = movieSimRepository.findByMovieX(movieX);
            if (neighbors == null || neighbors.isEmpty()) continue;


            for (MovieSimilarity s : neighbors) {
                Long movieY = s.getMovieY();
                if (movieUserMap.containsKey(movieY)) {
                    log.debug("Skipping movieY {} because user already rated it", movieY);
                    continue;
                }

                double sim = s.getSim();
                double ratingX = movieUserMap.get(movieX);
                MovieDetailsResponse details = tmdbService.getMovieDetails(movieY.intValue());
                if (details == null) {
                    log.warn("Movie {} does not exist on TMDB. Skipping.", movieY);
                    continue;
                }

                double tmdbWeight = details.getVote_average() / 10.0;
                double contributions = s.getSim() * movieUserMap.get(movieX) * tmdbWeight;


                scores.merge(movieY, contributions, Double::sum);
                log.debug("Contribution: X={}, Y={}, sim={}, tmdb={}, final={}",
                        movieX, movieY, s.getSim(), tmdbWeight, contributions);

            }
        }

        log.info("Collected {} candidate movies for scoring", scores.size());
        List<Long> recommendedMoviesId = scores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        log.info("Final recommended movie IDs: {}", recommendedMoviesId);


        return recommendedMoviesId.stream().map( id->
                    tmdbService.getMovieDetails(id.intValue())
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


    }


}
