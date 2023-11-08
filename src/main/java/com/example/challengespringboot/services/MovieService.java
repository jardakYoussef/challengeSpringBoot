package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getMovieFromApiWithPageNumber(int pageIteration) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTViMmM1ODcyOWVjYzMwMGIwZDBlYTU1MGU5YTQ1MyIsInN1YiI6IjY1NDJmZGI2M2UwMWVhMDEwMDIwY2FjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.V6n3VOEZGRInoZ1tpnxHa-cIMEwypEvnPmosoXnKqRQ");
        headers.set("accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> response = restTemplate.exchange("https://api.themoviedb.org/3/discover/movie?page=" + pageIteration + 1, HttpMethod.GET, entity, String.class);
        //either use object mapper or create another class

        String jsonResponse = response.getBody();

        // Use a JSON library such as Jackson to convert the JSON response to a Java object.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        // Get the JSON response as a JSON node.
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        JsonNode resultsArray = jsonNode.get("results");

        for (JsonNode movieNode : resultsArray) {
            Movie movie = objectMapper.readValue(movieNode.toString(), Movie.class);
            create(movie);
        }

        return objectMapper.readValue(jsonNode.get("total_pages").toString(), String.class);
    }

    public Movie findById(long movieId) throws Exception {
      Optional<Movie> movieOptional=  movieRepository.findById(movieId);
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
        return topTenMovies;
    }
}
