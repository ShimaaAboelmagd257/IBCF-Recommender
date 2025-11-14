package com.example.recs.repository;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<MovieEntity> findByGenresContainingIgnoreCase(String genre);

    List<MovieEntity> findByTitleContainingIgnoreCase(String title);
}
