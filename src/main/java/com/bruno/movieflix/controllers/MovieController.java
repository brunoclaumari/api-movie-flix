package com.bruno.movieflix.controllers;

import com.bruno.movieflix.dto.*;
import com.bruno.movieflix.entities.User;
import com.bruno.movieflix.repositories.UserRepository;
import com.bruno.movieflix.security.MyTokenService;
import com.bruno.movieflix.services.AuthenticationService;
import com.bruno.movieflix.services.MovieService;
import com.bruno.movieflix.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    @Autowired
    private MovieService service;


    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/findAll")
    public ResponseEntity<Page<MovieDTO>> findAll(
            Pageable pageable
            ,@RequestParam(value = "genreId", defaultValue = "0") Long genreId
            ,@RequestParam(value = "direction", defaultValue = "ASC") String direction
            ,@RequestParam(value = "orderByField", defaultValue = "title") String orderByField// vai ordenar pelo atributo 'title' da classe Movie
    ) {

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),  Direction.valueOf(direction), orderByField);

        Page<MovieDTO> list = service.findAllPaged(genreId, pageRequest);
        return ResponseEntity.ok().body(list);
    }


}
