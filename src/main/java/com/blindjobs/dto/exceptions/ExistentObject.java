package com.blindjobs.dto.exceptions;

public class ExistentObject extends Exception {
    public ExistentObject() {
    }

    public ExistentObject(String message) {
        super(message);
    }

    public ExistentObject(String message, Throwable cause) {
        super(message, cause);
    }

}
