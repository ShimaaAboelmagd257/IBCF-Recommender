package com.example.recs.repository;

import com.example.recs.domain.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RatingRepository extends JpaRepository<Rating,Rating.RatingId> {
    List<Rating> findByIdUserId(Long userId);
    List<Rating> findAll();
    List<Rating> findByIdMovieId(int movieId);
    List <Rating> findByIdUserIdIn(List<Long> userIds);
    @Query("SELECT r.id.userId FROM Rating r WHERE r.id.movieId = :movieId")
    List<Long> getUsersRatedMovie(@Param("movieId") Integer movieId);

    @Query("SELECT r.id.movieId FROM Rating r WHERE r.id.userId = :userId")
    List<Integer> getMoviesRatedByUser(@Param("userId") Long userId);

    //List<Rating> saveAll(List<Rating> ratings);
}
