package com.example.recs.service;

import com.example.recs.domain.entity.MovieSimilarity;
import com.example.recs.domain.entity.Rating;
import com.example.recs.repository.MovieSimRepository;
import com.example.recs.repository.RatingRepository;
import com.example.recs.util.GenreSim;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IBCFServiceImpl {


    private static final int MIN_COMMON_USERS = 3;
    private final RatingRepository ratingRepository;
    private final MovieSimRepository simRepository;
    private final MovieCache movieCache;

    private static final Logger log = LoggerFactory.getLogger(IBCFServiceImpl.class);

    @Transactional
    public void computeSim(int movieAId){

        Map<Long, Double> ratingMovieAMap = getRatingsMapForMovie(movieAId);
        if (ratingMovieAMap.isEmpty()) {
            log.warn("Movie {} has no ratings. Skipping similarity computation.", movieAId);
            return;
        }
        Set<Long> usersRatedMovieA = ratingMovieAMap.keySet();
        log.debug("MovieA ratings count = {}, users = {}", ratingMovieAMap.size(), usersRatedMovieA);
        Map<Integer,Map<Long,Double>> candidateMovie = buildCandidateMovieRatings(movieAId,usersRatedMovieA);
        simRepository.deleteByMovieX(movieAId);
        candidateMovie.forEach((movieBId,ratingsB) ->
                computeAndSaveSimilarities(movieAId,ratingMovieAMap,movieBId,ratingsB)
                );
    }


    private void computeAndSaveSimilarities(int movieAId, Map<Long, Double> ratingMovieAMap,  int movieBId,Map<Long, Double> movieBRatingMap) {
        int x = Math.min(movieAId, movieBId);
        int y = Math.max(movieAId, movieBId);

            Set<Long> commonUsers = findCommonUsers(ratingMovieAMap,movieBRatingMap);
            if (commonUsers.size() < MIN_COMMON_USERS) {
                log.warn("Computing similarities for movieA={} skipping because MIN_COMMON_USERS{}", commonUsers.size());
                return;
            }
            double ratingSim = cosine(ratingMovieAMap,movieBRatingMap,commonUsers);
            double genreSim = GenreSim.jaccardSim(movieCache.getGenres(movieAId),movieCache.getGenres(movieBId));
            log.info("Similarity computed: A={}, B={}, ratingSim={},genreSim={} ", movieAId, movieBId, ratingSim ,genreSim);
            double finalSim = (0.7*ratingSim) + (0.3 *genreSim);
            log.info("Final Similarity computed: A={}, B={}, finalSim={}", movieAId, movieBId, finalSim);
            simRepository.upsertSim(x,y,finalSim);

    }

    private Set<Long> findCommonUsers(Map<Long, Double> ratingMovieAMap, Map<Long, Double> movieBRatingMap) {
        Set<Long> commonUsers = new HashSet<>(ratingMovieAMap.keySet());
        log.debug("Comparing UserA={} with userB={}, commonUsers={}", ratingMovieAMap.keySet(), movieBRatingMap.keySet(), commonUsers.size());
        commonUsers.retainAll(movieBRatingMap.keySet());
        return commonUsers;
    }

    private Map<Integer, Map<Long, Double>> buildCandidateMovieRatings(int movieAId, Set<Long> usersRatedMovieA) {
        List<Rating> movieBRatings = ratingRepository.findByIdUserIdIn(new ArrayList<>(usersRatedMovieA));
        Map<Integer , Map<Long,Double>>  movieBUserRatingMap = new HashMap<>();

        for (Rating r: movieBRatings){
            Integer movieBId = r.getId().getMovieId();
            Long userId = r.getId().getUserId();
            if (movieBId== movieAId) continue;
            movieBUserRatingMap.computeIfAbsent(movieBId,x->new HashMap<>())
                    .put(userId,r.getRating());
        }
        log.info("Found {} candidate movies to compare with movieA={}", movieBUserRatingMap.size(), movieAId);
        return movieBUserRatingMap;
    }

    public Map<Long, Double> getRatingsMapForMovie(Integer movieId) {
        List<Rating> list = ratingRepository.findByIdMovieId(movieId);

        return list.stream()
                .collect(Collectors.toMap(
                        r -> r.getId().getUserId(),
                        Rating::getRating
                ));
    }

    private double cosine(Map<Long, Double> a, Map<Long, Double> b, Set<Long> users) {
        double dot = 0, normA = 0, normB = 0;

        for (Long u : users) {
            double ra = a.get(u);
            double rb = b.get(u);

            dot += ra * rb;
            normA += ra * ra;
            normB += rb * rb;
        }

        if (normA == 0 || normB == 0) return 0;

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}

