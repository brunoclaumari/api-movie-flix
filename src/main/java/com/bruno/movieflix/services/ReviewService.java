package com.bruno.movieflix.services;

import com.bruno.movieflix.dto.ReviewDTO;
import com.bruno.movieflix.dto.UserDTO;
import com.bruno.movieflix.entities.Review;
import com.bruno.movieflix.repositories.MovieRepository;
import com.bruno.movieflix.repositories.ReviewRepository;
import com.bruno.movieflix.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService{

    private static Logger logger = LoggerFactory.getLogger(ReviewService.class);


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public ReviewDTO insert(ReviewDTO dto) {
        Review entity = new Review();
        copyDtoToEntity(dto, entity);
        entity = reviewRepository.save(entity);

        return new ReviewDTO(entity);
    }




    private void copyDtoToEntity(ReviewDTO dto, Review entity) {

        UserDTO userDto = userService.getLoggedUser();

        logger.info(String.format("usuario logado: %s",userDto.getEmail()));
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        var movie = movieRepository.findById(dto.getMovie_id());
        if(movie.isPresent()) {
            logger.info(String.format("movie: %s",movie.get().getSubTitle()));
            entity.setMovie(movie.get());
        }

        var user = userRepository.findById(userDto.getId());
        if(user.isPresent())
            entity.setUser(user.get());

    }


}
