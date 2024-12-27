package com.bruno.movieflix.controllers.exceptions;

import com.bruno.movieflix.custom_exceptions.MyEmailAlreadyExistsException;
import com.bruno.movieflix.custom_exceptions.MyForbiddenException;
import com.bruno.movieflix.custom_exceptions.MyResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MyResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(MyResourceNotFoundException e, HttpServletRequest request){
        HttpStatus status=HttpStatus.NOT_FOUND;//404

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Recurso não encontrado!");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

/*    @ExceptionHandler(MyForbiddenException.class)
    public ResponseEntity<StandardError> forbidden1(MyForbiddenException e, HttpServletRequest request){
        HttpStatus status=HttpStatus.FORBIDDEN;//403

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Acesso Negado!");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
        HttpStatus status=HttpStatus.UNPROCESSABLE_ENTITY;//422->Entidade não pôde ser processada

        ValidationError err=new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Validation exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        for(FieldError f: e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MyEmailAlreadyExistsException.class)
    public ResponseEntity<ValidationError> validationEmail(MyEmailAlreadyExistsException e, HttpServletRequest request){
        HttpStatus status=HttpStatus.UNPROCESSABLE_ENTITY;//422->Entidade não pôde ser processada

        ValidationError err=new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Validation exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        for(FieldMessage f: e.getListErrors()) {
            err.addError(f.getFieldName(), f.getMessage());
        }

        return ResponseEntity.status(status).body(err);
    }


}
