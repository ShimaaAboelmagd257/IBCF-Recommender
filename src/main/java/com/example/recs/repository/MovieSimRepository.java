package com.example.recs.repository;


import com.example.recs.domain.entity.MovieSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieSimRepository extends JpaRepository<MovieSimilarity,Long> {
    List<MovieSimilarity> findByMovieX(Long movieX);
    Optional<MovieSimilarity> findByMovieXAndMovieY(Long movieX, Long MovieY);
    void deleteByMovieX(Long movieX);
}
