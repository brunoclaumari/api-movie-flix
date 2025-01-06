package com.bruno.movieflix.dto;

import com.bruno.movieflix.entities.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class GenreDTO {
    private Long id;

    @NotBlank(message = "Campo obrigatório!")
    private String name;

    @JsonIgnore
    Set<MovieDTO> movies = new HashSet<>();

    public GenreDTO() {

    }

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreDTO(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(Set<MovieDTO> movies) {
        this.movies = movies;
    }

}
