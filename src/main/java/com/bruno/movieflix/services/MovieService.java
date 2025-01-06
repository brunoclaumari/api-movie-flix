package com.bruno.movieflix.services;

import com.bruno.movieflix.dto.MovieDTO;
import com.bruno.movieflix.entities.Genre;
import com.bruno.movieflix.entities.Movie;
import com.bruno.movieflix.repositories.GenreRepository;
import com.bruno.movieflix.repositories.MovieRepository;
import com.bruno.movieflix.repositories.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {

    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository repository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @Transactional(readOnly = true)
    public Page<MovieDTO> findAllPaged(Long genreId, PageRequest pageRequest) {
        List<Genre> genres = new ArrayList<>();
        genres = (genreId == 0) ? genres : Arrays.asList(genreRepository.getReferenceById(genreId));

        Page<Movie> list = repository.findWithFilterGenreId(genreId, genres, pageRequest);
        // Usando 'expressão lambda' para transferir Movie para MovieDTO
        return list.map(x -> new MovieDTO(x, x.getReviews()));
    }
}
