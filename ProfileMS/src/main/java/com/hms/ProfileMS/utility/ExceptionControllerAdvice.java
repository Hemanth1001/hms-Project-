package com.hms.ProfileMS.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hms.ProfileMS.exception.HMSException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {   
    
    @Autowired
    Environment environment;
    
    @ExceptionHandler(HMSException.class)
    public ResponseEntity<ErrorInfo> hmsExceptionHandler(HMSException ex) {

        ErrorInfo error = new ErrorInfo();
        error.setErrorMessage(environment.getProperty(ex.getMessage()));
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(
            error,
            HttpStatus.INTERNAL_SERVER_ERROR
        ); 
    }


    @ExceptionHandler({MethodArgumentNotValidException.class,ConstraintViolationException.class})
    public ResponseEntity<ErrorInfo> handleValidationException(Exception ex) {

        String errorMsg ;
        if(ex instanceof MethodArgumentNotValidException manv) {
            errorMsg = manv.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        }
        else {
           ConstraintViolationException cve = (ConstraintViolationException) ex;
            errorMsg = cve.getConstraintViolations()
                    .stream()
                    .map(cv -> cv.getMessage())
                    .collect(Collectors.joining(", "));
        }
        ErrorInfo error = new ErrorInfo(
                errorMsg,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


   
}
