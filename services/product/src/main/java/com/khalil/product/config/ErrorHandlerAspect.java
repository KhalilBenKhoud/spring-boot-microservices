package com.khalil.product.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandlerAspect {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exp) {
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
