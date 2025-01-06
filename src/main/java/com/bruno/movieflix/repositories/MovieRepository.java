package com.bruno.movieflix.repositories;

import com.bruno.movieflix.entities.Genre;
import com.bruno.movieflix.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT mov FROM Movie mov "
            + "INNER JOIN mov.genre gen WHERE "
            + "( :genreId <= 0 OR gen IN :genres ) ")
            //+ "(COALESCE(:genres) IS NULL OR gen IN :genres);")
    Page<Movie> findWithFilterGenreId(Long genreId, List<Genre> genres, Pageable pageable);

}