package com.example.recs.repository;

import com.example.recs.domain.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Rating.RatingId> {
    List<Rating> findByIdUserId(Long userId);
    List<Rating> findAll();
    List<Rating> findByIdMovieId(Long movieId);
    List <Rating> findByIdUserIdIn(List<Long> userIds);
}
