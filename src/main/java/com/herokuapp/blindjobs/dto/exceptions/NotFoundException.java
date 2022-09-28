package com.herokuapp.blindjobs.dto.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException() {
    }

    public NotFoundException(String not) {

    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
