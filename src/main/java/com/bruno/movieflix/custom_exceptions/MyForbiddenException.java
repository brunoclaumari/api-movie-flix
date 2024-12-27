package com.bruno.movieflix.custom_exceptions;

import com.bruno.movieflix.controllers.exceptions.FieldMessage;

import java.util.ArrayList;
import java.util.List;

public class MyForbiddenException extends RuntimeException {


    public MyForbiddenException(String msg) {
        super(msg);
    }

}
