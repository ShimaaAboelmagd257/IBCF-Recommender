package com.example.recs.repository;

import com.example.recs.domain.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<MovieEntity,Long> {
// List<Long> findAllMovieId();
}
