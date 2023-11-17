package com.example.challengespringboot.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OmdbSearchResult {

    @JsonProperty
    private List<Movie> results;

    public List<Movie> getMovies() {
        return results;
    }
}