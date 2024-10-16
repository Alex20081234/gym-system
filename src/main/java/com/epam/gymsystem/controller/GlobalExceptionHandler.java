package com.epam.gymsystem.controller;

import com.epam.gymsystem.common.AccessDeniedRuntimeException;
import com.epam.gymsystem.common.TrainingObjectNotFoundException;
import com.epam.gymsystem.common.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TrainingObjectNotFoundException.class)
    public ResponseEntity<String> handleTrainingObjectNotFoundException(TrainingObjectNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedRuntimeException.class)
    public ResponseEntity<String> handleAccessDeniedRuntimeException(AccessDeniedRuntimeException e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, WebRequest request) {
        logger.severe(e.getMessage());
        return new ResponseEntity<>("Unexpected error occurred : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
