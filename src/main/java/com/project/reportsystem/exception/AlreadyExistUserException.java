package com.project.reportsystem.exception;

public class AlreadyExistUserException extends RuntimeException {
    public AlreadyExistUserException(String message) {
        super(message);
    }
}
