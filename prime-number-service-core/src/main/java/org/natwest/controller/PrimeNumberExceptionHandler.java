package org.natwest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.natwest.model.ErrorInfo;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

/*
Single class to handle all the exception and provide a standard response back.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PrimeNumberExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorInfo> handleInvalidInput(HttpServletRequest req, IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorInfo(reqUrl(req), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorInfo> handleNotANumber(HttpServletRequest req, NumberFormatException e) {
        return new ResponseEntity<>(new ErrorInfo(reqUrl(req), "Not a valid number."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ResponseEntity<?> handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorInfo errorInfo = new ErrorInfo("", "Not a valid acceptable representation. Valid ones are " +
                Arrays.toString(e.getDetailMessageArguments()));
        String errorInfoString = objectMapper.writeValueAsString(errorInfo.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(errorInfoString);
    }

    private String reqUrl(HttpServletRequest req) {
        String queryParams = req.getQueryString() == null ? "" : "?" + req.getQueryString();
        return req.getRequestURL().append(queryParams).toString();
    }

}
