package com.bruno.movieflix.controllers;

import com.bruno.movieflix.dto.LoginDTO;
import com.bruno.movieflix.dto.RoleDTO;
import com.bruno.movieflix.dto.UserRegisterDTO;
import com.bruno.movieflix.services.AuthenticationService;
import com.bruno.movieflix.util_tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String visitorUsername;
    private String visitorPassword;
    private String memberUsername;
    private String memberPassword;
    private String emptyPassword = "";
    private String invalidEmail = "..@invalido.com";

    @BeforeEach
    void setUp() throws Exception {

        visitorUsername = "bob@gmail.com";
        visitorPassword = "123456";
        memberUsername = "ana@gmail.com";
        memberPassword = "123456";

    }

    @Test
    public void loginShouldReturnTokenWhenUserDataIsValid() throws Exception {

        //String accessToken = tokenUtil.obtainAccessToken(mockMvc, visitorUsername, visitorPassword);

        String loginContent = tokenUtil.getStringLoginDTO(visitorUsername, visitorPassword);

        ResultActions result =
                mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(loginContent) //loginContent
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.email").value(visitorUsername));
        result.andExpect(jsonPath("$.token").isNotEmpty());
        result.andExpect(jsonPath("$.expires_in").isNotEmpty());
    }

    @Test
    public void loginShouldReturn422WhenPasswordIsBlank() throws Exception {

        String jsonBody = tokenUtil.getStringLoginDTO(visitorUsername, emptyPassword);

        ResultActions result =
                mockMvc.perform(post("/users/login")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("password"));
        result.andExpect(jsonPath("$.errors[0].message").value("Senha tem que ter de 6 a 10 caracteres"));
    }

    @Test
    public void loginShouldReturn422WhenEmailIsNotValid() throws Exception {

        String jsonBody = tokenUtil.getStringLoginDTO(invalidEmail, memberPassword);

        ResultActions result =
                mockMvc.perform(post("/users/login")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
        result.andExpect(jsonPath("$.errors[0].message").value("Favor entrar com um email válido!!"));
    }

    @Test
    public void registerShouldInsertResourceWhenMemberLoggedAndCorrectData() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, memberUsername, memberPassword);
        UserRegisterDTO userRegister = new UserRegisterDTO(null,"User 1","user1@user1.com","123456");
        userRegister.getRoles().add(new RoleDTO(1L, ""));

        String jsonBody = objectMapper.writeValueAsString(userRegister);

        ResultActions result =
                mockMvc.perform(post("/users/register")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value("User 1"));
        result.andExpect(jsonPath("$.email").value("user1@user1.com"));
        result.andExpect(jsonPath("$.roles").isNotEmpty());
    }

    @Test
    public void registerShouldReturn403WhenVisitorLoggedAndCorrectData() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, visitorUsername, visitorPassword);
        UserRegisterDTO userRegister = new UserRegisterDTO(null,"User 1","user1@user1.com","123456");
        userRegister.getRoles().add(new RoleDTO(1L, ""));

        String jsonBody = objectMapper.writeValueAsString(userRegister);

        ResultActions result =
                mockMvc.perform(post("/users/register")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());

    }

    @Test
    public void registerShouldReturn401WhenNoUserLoggedIn() throws Exception {

        UserRegisterDTO userRegister = new UserRegisterDTO(null,"User 1","user1@user1.com","123456");
        userRegister.getRoles().add(new RoleDTO(1L, ""));

        String jsonBody = objectMapper.writeValueAsString(userRegister);

        ResultActions result =
                mockMvc.perform(post("/users/register")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.message").value("Acesso negado: você precisa de um token válido para acessar esse recurso."));

    }
}
