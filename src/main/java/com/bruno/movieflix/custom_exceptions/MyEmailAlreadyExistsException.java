package com.bruno.movieflix.custom_exceptions;

import com.bruno.movieflix.controllers.exceptions.FieldMessage;

import java.util.ArrayList;
import java.util.List;

public class MyEmailAlreadyExistsException extends RuntimeException {

    private List<FieldMessage> listErrors = new ArrayList<>();

    public MyEmailAlreadyExistsException(String msg, List<FieldMessage> listErrors) {
        super(msg);
        this.listErrors = listErrors;
    }

    public List<FieldMessage> getListErrors() {
        return listErrors;
    }
}
