package com.example.recs.service;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.entity.Rating;
import com.example.recs.repository.MovieRepository;
import com.example.recs.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IBCFServiceImpl implements IBCFService{


    private final RatingRepository ratingRepository;
    private static final Logger log = LoggerFactory.getLogger(IBCFServiceImpl.class);

    @Override
    public List<Movie> recommendationForUser(Long userId){
        log.info("Recs Starting recommendation process for userId"+userId);
        var allRatings = ratingRepository.findAll();
        var userRatings = ratingRepository.findByIdUserId(userId);

        var itemUserMatrix = buildItemUserMatrix(allRatings);
        log.info("Recs itemUserMatrix "+ itemUserMatrix.size());
        var similarityMatrix = computeSimilarityMatrix(itemUserMatrix);
        log.info("Recs similarityMatrix "+ similarityMatrix.size());
        var predictions = predictScores(userRatings,itemUserMatrix,similarityMatrix);
        log.info("Recs predictions "+ predictions.size());

     return predictions.entrySet().stream()
             .sorted(Map.Entry.<Long,Double> comparingByValue().reversed()).limit(20)
             .map(entry -> {
                // Movie movie = movieRepository.findById(entry.getKey()).orElse(null);
                 Movie movie = new Movie();
                 movie.setId(entry.getKey().intValue());
                 movie.setTitle("Mock Movie " + entry.getKey());
                 log.info("Predicted Mock Movie ID: {}, Score: {}", entry.getKey(), entry.getValue());
                 /*if(movie == null) {
                     log.warn("Movie id {} not found in database" + entry.getKey());
                     return null;
                 }*/
                 movie.setPredictRating(entry.getValue());
                 log.info("Final  movies Recs : "+ movie.getTitle());
                 return movie;
             })
             .filter(Objects::nonNull)
             .toList();
    }

    private Map<Long,Map<Long,Double>> buildItemUserMatrix(List<Rating> allRatings){
        Map<Long,Map<Long,Double>> matrix = new HashMap<>();
        for (Rating rating:allRatings){
            matrix.computeIfAbsent(rating.getId().getMovieId(), k-> new HashMap<>())
                    .put(rating.getId().getUserId(), rating.getRating());
        }
        return matrix;
    }

    private Map<Long,Map<Long,Double>> computeSimilarityMatrix(Map<Long,Map<Long,Double>> itemUserMatrix){        Map<Long, Map<Long, Double>> simMatrix = new HashMap<>();

                for (Long a : itemUserMatrix.keySet()) {
                    for (Long b : itemUserMatrix.keySet()) {
                        if (a >= b) continue;
                        if (!haveCommonUser(itemUserMatrix.get(a), itemUserMatrix.get(b))) continue;
                        double sim = cosineSimilarity(itemUserMatrix.get(a), itemUserMatrix.get(b));
                        if (sim != 0) {
                            simMatrix.computeIfAbsent(a, k -> new HashMap<>()).put(b, sim);
                            simMatrix.computeIfAbsent(b, k -> new HashMap<>()).put(a, sim);
                        }
                    }
                }
                return simMatrix;
    }
    private boolean haveCommonUser(Map<Long, Double> a, Map<Long, Double> b) {
        for (Long userId : a.keySet()) {
            if (b.containsKey(userId)) return true;
        }
        return false;
    }
    private Map<Long, Double> predictScores(
            List<Rating> userRatings,
            Map<Long, Map<Long, Double>> itemUserMatrix,
            Map<Long, Map<Long, Double>> similarityMatrix) {


        Set<Long> seen = userRatings.stream()
                .map(r -> r.getId().getMovieId())
                .collect(Collectors.toSet());

        Map<Long, Double> predictions = new HashMap<>();

        for (Long movie : itemUserMatrix.keySet()) {
            if (seen.contains(movie)) continue;

            double numerator = 0, denominator = 0;
            for (Rating r : userRatings) {
                double sim = similarityMatrix
                        .getOrDefault(movie, Map.of())
                        .getOrDefault(r.getId().getMovieId(), 0.0);
                if (sim > 0) {
                    numerator += sim * r.getRating();
                    denominator += sim;
                }
            }
            if (denominator > 0)
                predictions.put(movie, numerator / denominator);
        }
        return predictions;
    }

    private double cosineSimilarity(Map<Long, Double> a, Map<Long, Double> b) {
        double dot = 0, normA = 0, normB = 0;
        for (Long user : a.keySet()) {
            double ra = a.get(user);
            normA += ra * ra;
            if (b.containsKey(user)) dot += ra * b.get(user);
        }
        for (double rb : b.values()) normB += rb * rb;
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}

