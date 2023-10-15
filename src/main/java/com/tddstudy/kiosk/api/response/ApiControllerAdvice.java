package com.tddstudy.kiosk.api.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getAllErrors().get(0).getDefaultMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> exception(Exception e) {
        log.error("", e);
        return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
