package com.example.challengespringboot.controllers;

import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.entities.OmdbSearchResult;
import com.example.challengespringboot.services.MovieService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Transactional
    @Retry(name = "fillDataBase", fallbackMethod = "getMoviesFrom")
    @CircuitBreaker(name = "fillDataBase", fallbackMethod = "getMoviesFrom")
    @GetMapping("/api/moviesCharge") // use post or put because we're creating
    public List<Movie> chargeMovies() throws IOException {
        List<Movie> results = new ArrayList<>();
        int pageNumber = 1;
        List<Movie> movieList = (movieService.getMovieFromApi());
        return movieList;
        //   return ResponseEntity.ok("Movies added successfully");


    }


    //@GetMapping("/api/moviesCharge1") // use post or put because we're creating

    public List<Movie> getMoviesFrom( Throwable throwable) {
        WebClient client = WebClient.create();
        Mono<OmdbSearchResult> mono = client.get()
                .uri("http://www.omdbapi.com/?i=tt3896198&apikey=d05e7a59")
                // .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTViMmM1ODcyOWVjYzMwMGIwZDBlYTU1MGU5YTQ1MyIsInN1YiI6IjY1NDJmZGI2M2UwMWVhMDEwMDIwY2FjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.V6n3VOEZGRInoZ1tpnxHa-cIMEwypEvnPmosoXnKqRQ")
                // .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(OmdbSearchResult.class);
        return mono.block().getMovies();
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @Cacheable("topMoviesCache")
    @GetMapping("/top-ten")
    public List<Movie> getTopTenMovies() {
        return movieService.getTopTenMovies();
    }
}
