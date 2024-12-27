package com.bruno.movieflix.security;


import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.bruno.movieflix.custom_exceptions.MyUnauthorizedException;
import com.bruno.movieflix.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private MyTokenService tokenService;

    @Autowired
    UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            var uri = request.getRequestURI();
            String token = this.recoverToken(request);
            if(token != null) {
                String login = tokenService.validateJwtToken(token);
                UserDetails user = userRepository.findByEmail(login);

                if(user != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(login, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            //if(!"/h2-console/**".contains(uri) && !"/favicon.ico".contains(uri))
            filterChain.doFilter(request, response);

        } catch (MyUnauthorizedException e) {
            // TODO: handle exception

            logger.info("exception no doFilterInternal do 'SecurityFilter'");
            logger.info(request.toString());

            CustomResponseHelper helper = new CustomResponseHelper(request, response, HttpStatus.UNAUTHORIZED);
            String userMessage = "Problema com o token fornecido. Faça um novo login com usuário válido.";

            helper.MakeCustomResponse(userMessage, e.getMessage());

/*            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(String.format("{\"mensagem\": \"%s\"}", e.getLocalizedMessage()));*/


        }
    }


    private String recoverToken(HttpServletRequest request) {

        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null) return null;

        return authHeader.replace("Bearer ", "");
    }

}

