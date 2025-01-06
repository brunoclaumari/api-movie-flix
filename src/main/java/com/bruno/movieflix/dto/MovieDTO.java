package com.bruno.movieflix.dto;

import com.bruno.movieflix.entities.Movie;
import com.bruno.movieflix.entities.Review;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class MovieDTO {

    private Long id;

    private String title;

    private String subTitle;

    @JsonProperty("movie_year")
    private Integer year;

    private String imgUrl;

    private String synopsis;

    //relações
    Set<ReviewDTO> reviews = new HashSet<>();

    //relações
    @JsonProperty("genre")
    private GenreDTO genreDTO;

    public MovieDTO() {

    }

    public MovieDTO(Long id, String title, String subTitle, Integer year, String imgUrl, String synopsis) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.year = year;
        this.imgUrl = imgUrl;
        this.synopsis = synopsis;
    }

    public MovieDTO(Movie movie) {

        this.id =  movie.getId();
        this.title =  movie.getTitle();
        this.subTitle = movie.getSubTitle();
        this.year = movie.getYear();
        this.imgUrl = movie.getImgUrl();
        this.synopsis = movie.getSynopsis();
        this.genreDTO = new GenreDTO(movie.getGenre());
    }

    public MovieDTO(Movie entity, Set<Review> reviews) {
        this(entity);
        reviews.forEach(rev -> this.reviews.add(new ReviewDTO(rev)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public GenreDTO getGenreDTO() {
        return genreDTO;
    }

    public void setGenreDTO(GenreDTO genreDTO) {
        this.genreDTO = genreDTO;
    }


    public Set<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

}
