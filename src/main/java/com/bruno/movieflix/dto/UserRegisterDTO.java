package com.bruno.movieflix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegisterDTO extends UserDTO {


    private String password;

    public UserRegisterDTO() {
        super();
    }

    public UserRegisterDTO(Long id, String name, String email, String password) {
        super(id, name, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
