package com.bruno.movieflix.util_tests;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bruno.movieflix.dto.LoginDTO;
import com.bruno.movieflix.dto.UserDTO;
import com.bruno.movieflix.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class TokenUtil {

    @Autowired
    private UserService userService;

    private UserDTO userLogged;

    public UserDTO getUserLogged() {
        return userLogged;
    }

    public String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {

/*        String clientId = "ola";
        String clientSecret = "amigo";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("username", username);
        params.add("password", password);*/

        LoginDTO dto = new LoginDTO(username, password);
        ObjectMapper om = new ObjectMapper();
        String login = om.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/users/login")
                        //.params(params)
                        //.with(httpBasic(clientId, clientSecret))
                        .content(login)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }

    public String getStringLoginDTO(String email, String password) throws JsonProcessingException {
        LoginDTO dto = new LoginDTO(email, password);
        ObjectMapper om = new ObjectMapper();

        return om.writeValueAsString(dto);
    }
}
