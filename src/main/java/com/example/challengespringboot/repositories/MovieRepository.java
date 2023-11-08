package com.example.challengespringboot.repositories;

import com.example.challengespringboot.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop10ByOrderByStarsDesc();
}
