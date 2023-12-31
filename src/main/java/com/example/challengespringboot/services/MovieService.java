package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.reactive.function.client.WebClient;


@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private final WebClient webClient;

    /*public Flux<Movie> getMovieFromApi() {
       Flux<Movie> movieFlux= webClient.get()
                .uri("/discover/movie?page=1")
               .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTViMmM1ODcyOWVjYzMwMGIwZDBlYTU1MGU5YTQ1MyIsInN1YiI6IjY1NDJmZGI2M2UwMWVhMDEwMDIwY2FjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.V6n3VOEZGRInoZ1tpnxHa-cIMEwypEvnPmosoXnKqRQ")
               .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
              //  .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                       // clientResponse -> Mono.error(Exception::new))
                .bodyToFlux(Movie.class).log();
               // .onErrorResume(Exception.class, e -> Flux.empty());

        movieFlux.subscribe(movie -> {
            System.out.println(movie.getTitle());
        });

        return movieFlux;
        Flux<Movie> movieFlux = webClient.get()
                .uri("/discover/movie?page=1")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTViMmM1ODcyOWVjYzMwMGIwZDBlYTU1MGU5YTQ1MyIsInN1YiI6IjY1NDJmZGI2M2UwMWVhMDEwMDIwY2FjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.V6n3VOEZGRInoZ1tpnxHa-cIMEwypEvnPmosoXnKqRQ")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()


                .bodyToFlux(Movie.class).log()
                .onErrorResume(Exception.class, e -> {
                    System.out.println("Error occurred: " + e.getMessage());
                    return Flux.empty();
                });


        return movieFlux;

    }
*/

    public MovieService(WebClient.Builder webClientBuilder, WebClient webClient) {

        this.webClient = webClient;
    }


    public List<Movie> getMovieFromApi() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Movie> movieList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders(); // put the bearer code in env file
        headers.set("Authorization", "Bearer    eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTViMmM1ODcyOWVjYzMwMGIwZDBlYTU1MGU5YTQ1MyIsInN1YiI6IjY1NDJmZGI2M2UwMWVhMDEwMDIwY2FjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.V6n3VOEZGRInoZ1tpnxHa-cIMEwypEvnPmosoXnKqRQ");
        headers.set("accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> response = restTemplate.exchange("https://api.thzemoviedb.org/3/discover/movie?page=" + 1, HttpMethod.GET, entity, String.class);
        //either use object mapper or create another class
        // change restTemplate to webClient.
        String jsonResponse = response.getBody();

        // Use a JSON library such as Jackson to convert the JSON response to a Java object.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        // Get the JSON response as a JSON node.
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        // do not use objectMapper
        JsonNode resultsArray = jsonNode.get("results");

        for (JsonNode movieNode : resultsArray) {
            Movie movie = objectMapper.readValue(movieNode.toString(), Movie.class);
            create(movie);
            movieList.add(movie);
        }
        return movieList;
        //     return objectMapper.readValue(jsonNode.get("total_pages").toString(), String.class);
    }


    public Movie findById(long movieId) throws Exception {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (!movieOptional.isPresent()) {
            throw new Exception("Movie with ID " + movieId + " does not exist");
        }
        return movieOptional.get();

    }

    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addStar(Movie movie) {
        movie.setStars(movie.getStars() + 1);
        return movieRepository.save(movie);
    }

    public Movie removeStar(Movie movie) throws Exception {
        if (movie.getStars() <= 0)
            throw new Exception("This movie +" + movie.getTitle() + "does not have any stars yet");
        movie.setStars(movie.getStars() - 1);
        return movieRepository.save(movie);
    }


    public List<Movie> getTopTenMovies() {
        List<Movie> topTenMovies = movieRepository.findTop10ByOrderByStarsDesc();
        simulateSlowService();
        return topTenMovies;
    }

    private void simulateSlowService() {
        try {
            long time = 5000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
