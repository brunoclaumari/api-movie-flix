package com.bruno.movieflix.controllers;

import com.bruno.movieflix.dto.ReviewDTO;
import com.bruno.movieflix.services.UserService;
import com.bruno.movieflix.util_tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private String visitorUsername;
    private String visitorPassword;
    private String memberUsername;
    private String memberPassword;

    @BeforeEach
    void setUp() throws Exception {

        visitorUsername = "bob@gmail.com";
        visitorPassword = "123456";
        memberUsername = "ana@gmail.com";
        memberPassword = "123456";
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenNotValidToken() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setText("Gostei do filme!");
        reviewDTO.setMovie_id(1L);

        String jsonBody = objectMapper.writeValueAsString(reviewDTO);

        ResultActions result =
                mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());

    }

    //String accessToken = tokenUtil.obtainAccessToken(mockMvc, memberUsername, memberPassword);

    @Test
    public void insertShouldInsertReviewWhenMemberLoggedAndCorrectData() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, memberUsername, memberPassword);

        //var user = userService.getLoggedUser();
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setText("Gostei muito do filme, recomendo!");
        reviewDTO.setMovie_id(1L);

        String jsonBody = objectMapper.writeValueAsString(reviewDTO);

        ResultActions result =
                mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.text").value(reviewDTO.getText()));
        result.andExpect(jsonPath("$.user['id']").exists());
        result.andExpect(jsonPath("$.movie_id").exists());

    }




}
