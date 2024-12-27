package com.bruno.movieflix.controllers;

import com.bruno.movieflix.dto.*;

import com.bruno.movieflix.entities.User;
import com.bruno.movieflix.repositories.UserRepository;
import com.bruno.movieflix.security.MyTokenService;
import com.bruno.movieflix.services.AuthenticationService;
import com.bruno.movieflix.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    AuthenticationService myAuthService;

    @Autowired
    AuthenticationManager authenticationManager;


   @Autowired
   UserRepository userRepository;

    @Autowired
    MyTokenService tokenService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/findAll")
    public ResponseEntity<Page<UserDTO>> findAll(
            Pageable pageable
            //@RequestParam(value = "page", defaultValue = "0") Integer page,
            //@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            ,@RequestParam(value = "direction", defaultValue = "ASC") String direction
            ,@RequestParam(value = "orderByField", defaultValue = "name") String orderByField// vai ordenar pelo atributo 'name' da classe User
    ) {

        //PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderByField);
        //PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),  Direction.valueOf(direction), orderByField);


        Page<UserDTO> list = service.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO data) {

        UserResponseDTO userResponseDTO = myAuthService.register(data);
/*        if(userResponseDTO.errorMessage() != null) //se retornou ID null, é porque já tem o usuário
            ResponseEntity.badRequest().body(userResponseDTO);*/

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userResponseDTO.getEmail()).toUri();

        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO data) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = this.authenticationManager.authenticate(userNamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        AuthResponseDTO resp = new AuthResponseDTO(auth.getName(), token, tokenService.getExpiresIn());

        return ResponseEntity.ok().body(resp);
    }
}
