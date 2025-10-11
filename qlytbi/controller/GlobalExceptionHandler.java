package com.qlph.qlytbi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex) {
        log.error("===> [ExceptionHandler] 404 Not Found: {}", ex.getRequestURL());
        return "redirect:/thietbi?error=Duong dan khong hop le";
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(TypeMismatchException ex) {
        log.error("===> [ExceptionHandler] 400 Bad Request: Invalid parameter {}", ex.getValue());
        return "redirect:/thietbi?error=Duong dan khong hop le";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex) {
        log.error("===> [ExceptionHandler] Unexpected error: {}", ex.getMessage(), ex);
        return "redirect:/thietbi?error=Duong dan khong hop le";
    }
}