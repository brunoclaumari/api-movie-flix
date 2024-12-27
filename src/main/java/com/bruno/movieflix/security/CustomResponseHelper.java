package com.bruno.movieflix.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CustomResponseHelper {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpStatus httpStatus;
    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomResponseHelper(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus) {
        this.request = request;
        this.response = response;
        this.httpStatus = httpStatus;
    }

    //HttpServletRequest request, HttpServletResponse response

    public void MakeCustomResponse(String userErrorMessage, String exceptionMessage ) throws IOException {

        if(this.request != null && this.response != null && this.httpStatus != null){
            this.response.setContentType("application/json; charset=utf-8;");
            this.response.setCharacterEncoding("UTF-8");
            //String mensagem = "Acesso negado: você precisa de um token válido para acessar esse recurso.";

            HttpStatus httpStatus = this.httpStatus;

            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", Instant.now().toString());
            data.put("status", httpStatus.value());
            data.put("error", exceptionMessage);
            data.put("message",userErrorMessage);
            data.put("path",request.getRequestURI());

            // setting the response HTTP status code
            response.setStatus(httpStatus.value());

            // serializing the response body in JSON
            response.getWriter().write(objectMapper.writeValueAsString(data));
        }
    }
}
