package com.bruno.movieflix.custom_exceptions;

public class MyUnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MyUnauthorizedException(String msg) {
        super(msg);
    }
}
