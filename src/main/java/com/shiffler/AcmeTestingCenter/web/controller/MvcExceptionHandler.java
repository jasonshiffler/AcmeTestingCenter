/*
Handles the exceptions that are thrown due to actions in the web layer and returns an appropriate Response Entity
so the API user understands what caused the error condition
 */

package com.shiffler.AcmeTestingCenter.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class MvcExceptionHandler {

    /**
     * This method is used to handle when data past into the controller doesn't conform to the validation constraint
     * annonations that have been applied to the entity.
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class) //This is the Exception that is thrown when a validation fails
    public ResponseEntity<List> validationErrorHandler(ConstraintViolationException e) {

        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());
        e.getConstraintViolations()
                .forEach(constraintViolation -> {
                    errors.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage());
                });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //This is the Exception that is thrown when a validation fails
    public ResponseEntity<List> validationBindException(MethodArgumentNotValidException e) {

        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }


}
