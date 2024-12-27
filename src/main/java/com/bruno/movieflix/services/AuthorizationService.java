package com.bruno.movieflix.services;

import com.bruno.movieflix.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails user = repository.findByEmail(username);
        if(user == null) {
            logger.error("Usuário não encontrado: " + username);
            throw new UsernameNotFoundException("Email não encontrado");
        }

        logger.info("User found: " + username);

        return user;
    }



}
