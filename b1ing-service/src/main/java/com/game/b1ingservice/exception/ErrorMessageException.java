package com.game.b1ingservice.exception;

import com.game.b1ingservice.commons.Constants;
import org.springframework.http.HttpStatus;


public class ErrorMessageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Constants.ERROR code;
	private Object params[];
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

	public ErrorMessageException() {
	}

	public ErrorMessageException(Constants.ERROR code, Object...params) {
		this.code = code;
		this.params = params;
	}

	public ErrorMessageException(Constants.ERROR code, HttpStatus httpStatus, Object...params) {
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
	
	public Constants.ERROR getCode() {
		return code;
	}

	public Object[] getParams() {
		return params;
	}
	
}
