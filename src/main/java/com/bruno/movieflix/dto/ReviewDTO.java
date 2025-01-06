package com.bruno.movieflix.dto;

import com.bruno.movieflix.entities.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

public class ReviewDTO {

    private Long id;

    @NotBlank(message = "Não pode deixar este campo em branco")
    private String text;

    private Long movie_id;

    private UserDTO user;

    @JsonIgnore
    private MovieDTO movieDTO;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, String authority) {
        this.id = id;
        this.text = authority;
    }

    public ReviewDTO(Review review) {
        id = review.getId();
        text = review.getText();
        this.movie_id = review.getMovie().getId();
        this.user = new UserDTO(review.getUser());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

/*    public MovieDTO getMovieDTO() {
        return movieDTO;
    }*/

    public void setMovieDTO(MovieDTO movieDTO) {
        this.movieDTO = movieDTO;
    }

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }
}
