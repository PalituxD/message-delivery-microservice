package org.justfoolingaround.apps.messagedelivery.web.config;

import org.justfoolingaround.apps.messagedelivery.web.base.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFoundException(ResourceNotFoundException ex) {

        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .timestamp(Calendar.getInstance().getTime())
                .message(ex.getMessage())
                .details(ex.getDetail())
                .build();

        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity handleValidationExceptions(BindException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .timestamp(Calendar.getInstance().getTime())
                .message(errors.toString())
                .details(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity globleExceptionHandler(Exception ex, WebRequest request) {

        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .timestamp(Calendar.getInstance().getTime())
                .message(ex.getMessage())
                .details(request.getDescription(Boolean.FALSE))
                .build();

        return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}