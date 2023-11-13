package com.example.challengespringboot.controllers;

import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.services.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Transactional
    @GetMapping("/api/moviesCharge") // use post or put because we're creating
    public ResponseEntity<String> chargeMovies() throws IOException {
        List<Movie> results = new ArrayList<>();
        int pageNumber = 1;
           String pageNumbers = (movieService.getMovieFromApi());
            // int currentPageNumber = Integer.valueOf(pageNumbers);


       /* do {
            String pageNumbers = movieService.getMovieFromApiWithPageNumber(pageNumber);
            maxPageNumber = Integer.valueOf(pageNumbers);
           pageNumber++;
        }while(maxPageNumber>pageNumber); */
       /* WebClient webClient = WebClient.create("https://imdb-api.com/en/API/IMDbList/k_9vg7s2y7");

        Flux<Movie> movieFlux= webClient.get()
                .uri("/ls004285275")
                .retrieve()
                .bodyToFlux(Movie.class);*/
        return ResponseEntity.ok("Movies added successfully");
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @Cacheable("topMoviesCache")

    @GetMapping("/top-ten")
    public List<Movie> getTopTenMovies() {
     return   movieService.getTopTenMovies();
    }
}
