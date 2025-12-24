package com.example.recs.service;

import com.example.recs.domain.dto.MovieDetailsResponse;
import com.example.recs.domain.dto.MovieDto;
import com.example.recs.domain.entity.MovieSimilarity;
import com.example.recs.domain.entity.Rating;
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
    private final UserSimService userSimService;
   private final MovieCache movieCache;
    private static final Logger log = LoggerFactory.getLogger(RecommendationsService.class);




    public List<MovieDto> recommendationForUser(Long userId , int limit){
        log.info("Generating recommendations for user {}", userId);

        // Get user Ratings
        Map<Integer,Double> movieUserMap = getMovieRatings(userId);

        Map<Integer,Double> scores = new HashMap<>();

        // 2.Map through all user rated movies
        for (Map.Entry<Integer,Double> rated : movieUserMap.entrySet()){
            int movieX = rated.getKey();
            double ratingX = rated.getValue();
            List<MovieSimilarity> neighbors = movieSimRepository.findByMovieX(movieX);
            if (neighbors == null || neighbors.isEmpty()) {
                log.warn("neighbors {} is empty or NULL for movieX id ", movieX);
                continue;
            }


            for (MovieSimilarity s : neighbors) {
                int movieY = s.getMovieY();
                if (movieUserMap.containsKey(movieY)) {
                    log.debug("Skipping movieY {} because user already rated it", movieY);
                    continue;
                }


                double itemSim = s.getSim();
                double userSim = userSimService.getUserSimForMovie(userId,movieY);
                MovieDetailsResponse details = movieCache.getCacheResponses(movieY);
                double tmdbWeight = details.getVote_average()/10.0;
                double finalScore  = 0.6*itemSim + 0.2*userSim + 0.2*tmdbWeight ;


                scores.merge(movieY, finalScore, Double::sum);
                log.debug("Contribution: X={}, Y={}, sim={}, tmdb={}, final={}",
                        movieX, movieY, s.getSim(), tmdbWeight, finalScore);

            }
        }

        log.info("Collected {} candidate movies for scoring", scores.size());
        List<Integer> recommendedMoviesId = scores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        log.info("Final recommended movie IDs: {}", recommendedMoviesId);

        return recommendedMoviesId.stream().map( id->
                {
                    return new MovieDto(id," ");
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


    }

    public Map<Integer,Double> getMovieRatings(Long userId){
        List<Rating> ratings = ratingRepository.findByIdUserId(userId);
        if (ratings.isEmpty()) {
            log.warn("User {} has no ratings. Returning empty recommendations.", userId);
        }
        return ratings.stream().collect(Collectors.toMap(
                r-> r.getId().getMovieId(), Rating::getRating
        ));
    }


}
