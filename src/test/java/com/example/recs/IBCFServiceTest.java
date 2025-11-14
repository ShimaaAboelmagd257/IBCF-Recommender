package com.example.recs;

import com.example.recs.domain.dto.MovieDto;
import com.example.recs.domain.entity.Rating;
import com.example.recs.repository.RatingRepository;
import com.example.recs.service.IBCFService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IBCFServiceTest {

    @Autowired
    private IBCFService ibcfService;

    @Autowired
    private RatingRepository ratingRepository;


}

