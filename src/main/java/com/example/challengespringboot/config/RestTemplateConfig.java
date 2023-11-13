package com.example.challengespringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient() {
        return   WebClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTViMmM1ODcyOWVjYzMwMGIwZDBlYTU1MGU5YTQ1MyIsInN1YiI6IjY1NDJmZGI2M2UwMWVhMDEwMDIwY2FjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.V6n3VOEZGRInoZ1tpnxHa-cIMEwypEvnPmosoXnKqRQ")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)

                .build();
    }


}

