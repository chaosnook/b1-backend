package com.game.b1ingservice.utils.exception;


import com.game.b1ingservice.payload.commons.ErrorObj;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class CommonErrorException extends RuntimeException {

    private List<ErrorObj> errors = new ArrayList<>();
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public CommonErrorException() {
    }

    public CommonErrorException(String message) {
        super(message);
    }

    public CommonErrorException(String message, List<ErrorObj> errors) {
        super(message);
        this.errors = errors;
    }

    public CommonErrorException(String message, List<ErrorObj> errors, HttpStatus httpStatus) {
        super(message);
        this.errors = errors;
        this.httpStatus = httpStatus;
    }

    public CommonErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public List<ErrorObj> getErrors() {
        return errors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

