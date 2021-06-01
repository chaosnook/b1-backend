package com.game.b1ingservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.ApiResponse;
import com.game.b1ingservice.payload.commons.ErrorObj;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint {

//    private final GrpcMessageService messageService;

    private static final String STATUS_RES = "status";
    private static final String MESSAGE_RES = "message";
    private static final String ERROR_RES = "errors";
    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_99999.msg);

        resError.setErrors(new ArrayList<>(Collections.singletonList(new ErrorObj(Constants.ERROR.ERR_TOKEN.name(), Constants.ERROR.ERR_TOKEN.msg))));

        response.getWriter().write(mapper.writeValueAsString(resError));
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage((ex.getMessage() != null && ex.getMessage().length() > 0) ? String.format("Parameter [%s] is missing.", ex.getParameterName()) : "Request is in malformed format or missing required parameter(s).");
        resError.setErrors(new ArrayList<>());
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, resError);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_99999.msg);
        resError.setErrors(new ArrayList<>());
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, resError);
    }

    @SneakyThrows
    @ExceptionHandler(CommonErrorException.class)
    public final ResponseEntity handleCommonErrorException(CommonErrorException ex, WebRequest request) {
        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(ex.getMessage());
        resError.setErrors(ex.getErrors());
        return ResponseHelper.response(ex.getHttpStatus(), resError);
    }

    @SneakyThrows
    @ExceptionHandler(ErrorMessageException.class)
    public final ResponseEntity handleErrorMessageException(ErrorMessageException ex, WebRequest request) {
        List<ErrorObj> errors = new ArrayList<>();

        if (ex.getCode() != null) {
            ErrorObj error = new ErrorObj();
            error.setCode(ex.getCode().name());
            error.setDescription(ex.getCode().msg);
            errors.add(error);
        }

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_99999.msg);
        resError.setErrors(errors);

        return ResponseHelper.response(ex.getHttpStatus(), resError);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity handleBadRequestException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(ex.getMessage() != null && ex.getMessage().length() > 0 ? ex.getLocalizedMessage() : "error");
        resError.setErrors(new ArrayList<>());
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, resError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_00001.msg);

        return ResponseHelper.response(HttpStatus.BAD_REQUEST, resError);
    }

    @ExceptionHandler(JWTException.class)
    public final ResponseEntity handleJWTException(JWTException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(ex.getMessage() != null && ex.getMessage().length() > 0 ? ex.getLocalizedMessage() : "error");
        resError.setErrors(new ArrayList<>());
        return ResponseHelper.response(HttpStatus.BAD_REQUEST, resError);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public final ResponseEntity<Object> handleOptimisticLockException(OptimisticLockingFailureException ex, WebRequest request) {

        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_99999.msg);
        resError.setErrors(new ArrayList<>(Collections.singletonList(new ErrorObj(Constants.ERROR.ERR_00002.name(), Constants.ERROR.ERR_00002.msg))));

        return ResponseHelper.response(HttpStatus.PRECONDITION_FAILED, resError);
    }

    @ExceptionHandler(DuplicateLoginException.class)
    public final ResponseEntity handleDuplicateLoginException(DuplicateLoginException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_77777.msg);
        resError.setErrors(new ArrayList<>());
        return ResponseHelper.response(HttpStatus.UNAUTHORIZED, resError);
    }

    @SneakyThrows
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public final ResponseEntity<Object> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex, WebRequest request) {
        log.error("handleInvalidDataAccessResourceUsageException :",ex);
        ApiResponse resError = new ApiResponse();
        resError.setStatus(false);
        resError.setMessage(Constants.ERROR.ERR_99999.msg);
        resError.setErrors(new ArrayList<>(Collections.singletonList(new ErrorObj(Constants.ERROR.ERR_00000.name(), Constants.ERROR.ERR_00000.msg))));

        return ResponseHelper.response(HttpStatus.PRECONDITION_FAILED, resError);
    }

}
