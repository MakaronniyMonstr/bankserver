package com.vesko.application.controller;

import com.vesko.application.exception.JsonParsableException;
import com.vesko.application.exception.ResourceCannotBeModifiedException;
import com.vesko.application.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdviceController extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public Map resourceNotFoundExceptionHandler(JsonParsableException ex) {
        return ex.getJsonSerializableMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ResourceCannotBeModifiedException.class})
    public Map resourceCannotBeModified(JsonParsableException ex) {
        return ex.getJsonSerializableMessage();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.of(
                Optional.of(
                        ex.getBindingResult().getFieldErrors().stream()
                                .collect(
                                        Collectors.groupingBy(
                                                FieldError::getField,
                                                Collectors.mapping(
                                                        DefaultMessageSourceResolvable::getDefaultMessage,
                                                        Collectors.toList()
                                                )
                                        )
                                )
                )
        );
    }
}
