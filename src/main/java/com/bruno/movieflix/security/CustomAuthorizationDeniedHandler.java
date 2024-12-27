package com.bruno.movieflix.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthorizationDeniedHandler implements AuthenticationEntryPoint {

    private static Logger logger = LoggerFactory.getLogger(CustomAuthorizationDeniedHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Você pode customizar a resposta para dar um retorno apropriado

        CustomResponseHelper helper = new CustomResponseHelper(request, response, HttpStatus.UNAUTHORIZED);
        String userMessage = "Acesso negado: você precisa de um token válido para acessar esse recurso.";

        helper.MakeCustomResponse(userMessage, authException.getMessage());

        /*response.setContentType("application/json; charset=utf-8;");
        response.setCharacterEncoding("UTF-8");
        String mensagem = "Acesso negado: você precisa de um token válido para acessar esse recurso.";

        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED; // 401

        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", Instant.now().toString());
        data.put("status", httpStatus.value());
        data.put("error",authException.getMessage());
        data.put("message",mensagem);
        data.put("path",request.getRequestURI());

        // setting the response HTTP status code
        response.setStatus(httpStatus.value());

        // serializing the response body in JSON
        response.getWriter().write(objectMapper.writeValueAsString(data));*/
    }
}

