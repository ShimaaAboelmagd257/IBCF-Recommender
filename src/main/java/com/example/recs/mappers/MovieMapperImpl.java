package com.example.recs.mappers;

import com.example.recs.domain.dto.MovieDto;
import com.example.recs.domain.entity.MovieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements Mapper<MovieEntity,MovieDto>{

   private ModelMapper mapper;

    public MovieMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MovieDto toDto(MovieEntity movieEntity) {
        return mapper.map(movieEntity,MovieDto.class);
    }

    @Override
    public MovieEntity fromDto(MovieDto movieDto) {
        return mapper.map(movieDto,MovieEntity.class);
    }


}
