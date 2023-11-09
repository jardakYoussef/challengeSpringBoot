package com.example.challengespringboot.controllers;

import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.services.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        for (int i = 0; i < pageNumber; i++) {
            String pageNumbers = movieService.getMovieFromApiWithPageNumber(i);
            int currentPageNumber = Integer.valueOf(pageNumbers);
            if (currentPageNumber <= 500)
                pageNumber = currentPageNumber;
            else
                pageNumber = 500;
            pageNumber = 2;
            //just get the first page
        }
        int maxPageNumber= 1;

       /* do {
            String pageNumbers = movieService.getMovieFromApiWithPageNumber(pageNumber);
            maxPageNumber = Integer.valueOf(pageNumbers);
           pageNumber++;
        }while(maxPageNumber>pageNumber); */

        return ResponseEntity.ok("Movies added successfully");
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    @GetMapping("/top-ten")
    public List<Movie> getTopTenMovies() {
     return   movieService.getTopTenMovies();
    }
}
