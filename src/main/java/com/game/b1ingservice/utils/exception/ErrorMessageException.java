package com.game.b1ingservice.utils.exception;

import org.springframework.http.HttpStatus;

public class ErrorMessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private Object params[];
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ErrorMessageException() {
    }

    public ErrorMessageException(String code, Object...params) {
        this.code = code;
        this.params = params;
    }

    public ErrorMessageException(String code, HttpStatus httpStatus, Object...params) {
        this.code = code;
        this.params = params;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public Object[] getParams() {
        return params;
    }

}


