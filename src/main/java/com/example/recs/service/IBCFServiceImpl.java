package com.example.recs.service;

import com.example.recs.domain.entity.MovieSimilarity;
import com.example.recs.domain.entity.Rating;
import com.example.recs.repository.MovieSimRepository;
import com.example.recs.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IBCFServiceImpl {


    private static final int MIN_COMMON_USERS = 1;
    private final RatingRepository ratingRepository;
    private final MovieSimRepository simRepository;
    private final TmdbService tmdbService;

    private static final Logger log = LoggerFactory.getLogger(IBCFServiceImpl.class);

    @Transactional
    public void computeSim(Long movieAId){
        log.info("Computing similarities for movieA={}", movieAId);

        if (!tmdbService.exists(movieAId)){
            log.warn("Computing similarities for movieA={} don't exist",movieAId);
            return;
        }

        List<Rating> ratingsMovieA = ratingRepository.findByIdMovieId(movieAId);
        if (ratingsMovieA.isEmpty()) {
            log.warn("Movie {} has no ratings. Skipping similarity computation.", movieAId);
            return;
        }

        Map<Long, Double> ratingMovieAMap = new HashMap<>();
        List<Long> usersRatedMovieA = new ArrayList<>();

        for (Rating r : ratingsMovieA){
            ratingMovieAMap.put(r.getId().getUserId(), r.getRating());
            usersRatedMovieA.add(r.getId().getUserId());
        }

        log.debug("MovieA ratings count = {}, users = {}", ratingMovieAMap.size(), usersRatedMovieA);
        List<Rating> otherRatings = ratingRepository.findByIdUserIdIn(usersRatedMovieA);
        Map<Long , Map<Long,Double>>  movieBUserRatingMap = new HashMap<>();
        for (Rating r:otherRatings){
            Long movieBId = r.getId().getMovieId();
            Long userId = r.getId().getUserId();
            if (movieBId.equals(movieAId)) continue;
            movieBUserRatingMap.computeIfAbsent(movieBId,x->new HashMap<>())
                    .put(userId,r.getRating());
        }
        log.info("Found {} candidate movies to compare with movieA={}", movieBUserRatingMap.size(), movieAId);

        simRepository.deleteByMovieX(movieAId);


        for (Map.Entry<Long,Map<Long,Double>> entry:movieBUserRatingMap.entrySet()){
            Long movieBId = entry.getKey();
            Map<Long,Double> movieBRatingMap = entry.getValue();


            Set<Long> commonUsers = new HashSet<>(ratingMovieAMap.keySet());
            log.debug("Comparing A={} with B={}, commonUsers={}", movieAId, movieBId, commonUsers.size());
            commonUsers.retainAll(movieBRatingMap.keySet());
            if (commonUsers.size() < MIN_COMMON_USERS) {
                log.warn("Computing similarities for movieA={} skipping because MIN_COMMON_USERS{}",commonUsers.size());
                continue;
            }

            double sim = cosine(ratingMovieAMap,movieBRatingMap,commonUsers);
            log.info("Similarity computed: A={}, B={}, sim={}", movieAId, movieBId, sim);
            if (Double.isFinite(sim)){
                simRepository.save(MovieSimilarity.builder().movieX(movieAId).movieY(movieBId).sim(sim).build());
            }

        }







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

