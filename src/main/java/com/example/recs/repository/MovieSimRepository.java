package com.example.recs.repository;


import com.example.recs.domain.entity.MovieSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieSimRepository extends JpaRepository<MovieSimilarity,Long> {

    List<MovieSimilarity> findByMovieX(Integer movieX);
    Optional<MovieSimilarity> findByMovieXAndMovieY(Integer movieX, Integer MovieY);
    void deleteByMovieX(Integer movieX);
    @Modifying
    @Query(
            value = """
            INSERT INTO movies_similarity (movieX, movieY, sim)
            VALUES (:x, :y, :sim)
            ON CONFLICT (movieX, movieY)
            DO UPDATE SET sim = EXCLUDED.sim
            """,nativeQuery = true
    )
    void upsertSim(int x, int y, double sim);
}
