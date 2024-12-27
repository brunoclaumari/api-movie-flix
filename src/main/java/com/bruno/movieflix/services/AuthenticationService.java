package com.bruno.movieflix.services;

import com.bruno.movieflix.controllers.exceptions.FieldMessage;
import com.bruno.movieflix.custom_exceptions.MyEmailAlreadyExistsException;
import com.bruno.movieflix.dto.UserDTO;
import com.bruno.movieflix.dto.UserRegisterDTO;
import com.bruno.movieflix.dto.UserResponseDTO;
import com.bruno.movieflix.entities.Role;
import com.bruno.movieflix.entities.User;
import com.bruno.movieflix.repositories.RoleRepository;
import com.bruno.movieflix.repositories.UserRepository;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    @Transactional
    public UserResponseDTO register(@Valid UserRegisterDTO data) {

        User newUser = new User();

        if(!registerIsValid(data)) {
            List<FieldMessage> list = new ArrayList<>();
            String message = String.format("Email %s já existe!", data.getEmail());
            list.add(new FieldMessage("email", message));

            logger.error(message);
            throw new MyEmailAlreadyExistsException(message, list);
        }

        String encriptedPass = new BCryptPasswordEncoder().encode(data.getPassword());
        copyDtoToEntity(data, newUser);
        newUser.setPassword(encriptedPass);

        User returnUser = this.userRepository.save(newUser);
        logger.info(String.format("Usuário %s criado com sucesso!", data.getEmail()));

        return new UserResponseDTO(returnUser);
    }

    private boolean registerIsValid(UserRegisterDTO data){
        boolean isValid = this.userRepository.findByEmail(data.getEmail()) == null;
        return isValid;
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {

        entity.setName(dto.getName());
        //entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();

        dto.getRoles().forEach(roleDto -> {

            entity.getRoles().add(roleRepository.getReferenceById(roleDto.getId()));
        });
    }

}
