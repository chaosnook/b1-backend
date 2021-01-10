package com.game.b1ingservice.utils.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    private static final String STATUS_RES = "status";
    private static final String MESSAGE_RES = "message";
    private static final String ERROR_RES = "errors";
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put(STATUS_RES, false);
        res.put(MESSAGE_RES, (ex.getMessage() != null && ex.getMessage().length() > 0) ? String.format("Parameter [%s] is missing.", ex.getParameterName()) : "Request is in malformed format or missing required parameter(s).");
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, res);
    }



    @SneakyThrows
    @ExceptionHandler(CommonErrorException.class)
    public final ResponseEntity handleCommonErrorException(CommonErrorException ex, WebRequest request) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put(STATUS_RES, false);
        res.put(MESSAGE_RES, ex.getMessage());
        res.put(ERROR_RES, ex.getErrors());
        return ResponseHelper.response(ex.getHttpStatus(), res);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS_RES, false);
        response.put(MESSAGE_RES, "มีข้อมูลอยู่ในระบบแล้ว");
        response.put("request", request.getParameterMap());
        return ResponseHelper.response(HttpStatus.OK, response);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public final ResponseEntity<Object> handleOptimisticLockException(OptimisticLockingFailureException ex, WebRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS_RES, false);
        response.put(MESSAGE_RES, ex.getLocalizedMessage());
        return ResponseHelper.response(HttpStatus.PRECONDITION_FAILED, response);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity handleBadRequestException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS_RES, false);
        response.put(MESSAGE_RES, ex.getMessage() != null && ex.getMessage().length() > 0 ? ex.getLocalizedMessage() : "error");
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, response);
    }


    @ExceptionHandler(JWTException.class)
    public final ResponseEntity handleJWTException(JWTException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS_RES, false);
        response.put(MESSAGE_RES, ex.getMessage() != null && ex.getMessage().length() > 0 ? ex.getLocalizedMessage() : "error");
        response.put(ERROR_RES, null);
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, response);
    }

}