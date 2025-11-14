package com.example.recs.service;

import com.example.recs.domain.dto.Movie;
import com.example.recs.domain.dto.MovieDetailsResponse;
import com.example.recs.domain.dto.TmdbGenreResponse;
import com.example.recs.domain.dto.TmdbMoviesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TMDbClient implements TmdbService {

    @Value("${tmdb.api.token}")
    private String apiToken;
    private final RestTemplate restTemplate;
    private final static String BASE_URL = "https://api.themoviedb.org/3";
    public TMDbClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpEntity<String> buildEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("accept", "application/json");
        return new HttpEntity<>(headers);
    }
    @Override
    public TmdbMoviesResponse getPopularMovies(){
        String url = BASE_URL + "/movie/popular?language=en-US&page=1";
        ResponseEntity<TmdbMoviesResponse> response =
                restTemplate.exchange(url, HttpMethod.GET,buildEntity() ,TmdbMoviesResponse.class);
        return response.getBody();
    }
    @Override
    public TmdbGenreResponse getGenres(){

        String url = BASE_URL + "/genre/movie/list?language=en";
        ResponseEntity<TmdbGenreResponse> response =
                restTemplate.exchange(url, HttpMethod.GET,buildEntity() ,TmdbGenreResponse.class);
        return response.getBody();

    }
    @Override
    public TmdbMoviesResponse getMoviesByGenre(int genreId){
        String url = BASE_URL+ "/discover/movie?language=en&sort_by=vote_average.desc&vote_count.gte=200&with_genres=" +genreId;
        ResponseEntity<TmdbMoviesResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET,buildEntity() ,TmdbMoviesResponse.class);
        return responseEntity.getBody();
    }
    @Override
    public MovieDetailsResponse getMovieDetails(int movieId ){
        String url = BASE_URL+ "/movie/"+ movieId+"?language=en-US";
        ResponseEntity<MovieDetailsResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET,buildEntity() ,MovieDetailsResponse.class);
        return responseEntity.getBody();
    }



}
