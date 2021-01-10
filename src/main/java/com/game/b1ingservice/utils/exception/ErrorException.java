package com.game.b1ingservice.utils.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorException extends RuntimeException {

    private final Error error;
    private final HttpStatus httpStatus;

    public ErrorException(Error error) {
        this.error = error;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public ErrorException(Error error, HttpStatus httpStatus) {
        this.error = error;
        this.httpStatus = httpStatus;
    }

}
