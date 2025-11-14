package com.example.recs.repository;

import com.example.recs.domain.entity.LoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoadStatusRepository extends JpaRepository<LoadStatus,String> {
}
