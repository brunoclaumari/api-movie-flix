package com.bruno.movieflix.dto;

import com.bruno.movieflix.entities.User;

public class UserResponseDTO extends UserDTO{

    public UserResponseDTO(Long id, String name, String email ) {
        super(id, name, email);
    }

    public UserResponseDTO(User entity) {
        super(entity);
    }
}
