package com.khalil.customer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandlerAspect {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exp) {
        log.warn(exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMessage()) ;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle2(MethodArgumentNotValidException exp)
    {
        Map<String,String> errors  = new HashMap<>() ;
       exp.getBindingResult().getAllErrors().forEach(
               error -> {
                  var fieldName = ((FieldError) error).getField() ;
                  var errorMessage = error.getDefaultMessage() ;
                  errors.put(fieldName,errorMessage) ;
               }) ;


       return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errors)) ;

    }
}
