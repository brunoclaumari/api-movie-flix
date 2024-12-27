package com.bruno.movieflix.services;

import com.bruno.movieflix.dto.UserDTO;
import com.bruno.movieflix.entities.User;
import com.bruno.movieflix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {


    @Autowired
    UserRepository repository;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
        Page<User> list = repository.findAll(pageRequest);
        // Usando 'expressão lambda' para transferir User para UserDTO
        return list.map(x -> new UserDTO(x));
    }
}
