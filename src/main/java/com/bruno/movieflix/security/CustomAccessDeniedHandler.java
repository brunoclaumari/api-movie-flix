package com.bruno.movieflix.security;

import com.bruno.movieflix.custom_exceptions.MyForbiddenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Você pode customizar a resposta para dar um retorno apropriado

        CustomResponseHelper helper = new CustomResponseHelper(request, response, HttpStatus.FORBIDDEN);//403
        String userMessage = "Acesso negado: você precisa de uma permissão 'MEMBER' para este recurso.";

        helper.MakeCustomResponse(userMessage, accessDeniedException.getMessage());

/*        response.setContentType("application/json; charset=utf-8;");
        response.setCharacterEncoding("UTF-8");
        String mensagem = "Acesso negado: você precisa de uma permissão 'MEMBER' para este recurso.";

        HttpStatus httpStatus = HttpStatus.FORBIDDEN; // 403

        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", Instant.now().toString());
        data.put("status", httpStatus.value());
        data.put("error",accessDeniedException.getMessage());
        data.put("message",mensagem);
        data.put("path",request.getRequestURI());

        // setting the response HTTP status code
        response.setStatus(httpStatus.value());

        // serializing the response body in JSON
        response.getWriter().write(objectMapper.writeValueAsString(data));  */

    }
}

