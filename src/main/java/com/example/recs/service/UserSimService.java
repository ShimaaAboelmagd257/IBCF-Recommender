package com.example.recs.service;

import com.example.recs.domain.entity.Rating;
import com.example.recs.repository.RatingRepository;
import com.example.recs.util.GenreSim;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserSimService {
    private final RatingRepository ratingRepository;
    private final MovieCache movieCache;
    private static final Logger log = LoggerFactory.getLogger(UserSimService.class);

    public double getUserSimForMovie(Long userId, int movieY){
        log.info("getUserSimForMovie for movie "+movieY +"user Id "+userId);
        List<Long> usersRatedMovieY = ratingRepository.getUsersRatedMovie(movieY);

        double sum = 0.0;
        int count = 0;
        Set<Integer> genreY = movieCache.getGenres(movieY);
        log.info("getUserSimForMovie for movie "+movieY +"genreY.size  "+genreY.size());

        for (Long other: usersRatedMovieY){
            if (other == userId) continue;
            List<Integer> moviesIdByOther = ratingRepository.getMoviesRatedByUser(other);
            log.info("getUserSimForMovie for moviesIdByOther  "+moviesIdByOther.size());

            double max =0;
            for (int movieX: moviesIdByOther) {
                Set<Integer> genreX = movieCache.getGenres(movieX);
                double genreSim = GenreSim.jaccardSim(genreX,genreY);
                max = Math.max(max,genreSim);
            }
            sum+=max;
            count++;
        }
        log.info("getUserSimForMovie count is ", count);
        return count == 0?0: sum /count;
    }

}
