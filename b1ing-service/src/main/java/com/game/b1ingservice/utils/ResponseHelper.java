package com.game.b1ingservice.utils;

import com.game.b1ingservice.payload.commons.PageObject;
import com.game.b1ingservice.payload.commons.PageResponse;
import com.game.b1ingservice.payload.commons.PageSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResponseHelper {
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static ResponseEntity<Object> response(HttpStatus httpStatus, Object object) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        }

        return ResponseEntity.status(httpStatus).headers(httpHeaders).body(object);
    }
    public static ResponseEntity<Object> success(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("status", true);
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> successWithData(String message,Object obj) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("status", true);
        data.put("data", obj);
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> bad(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("status", false);
        return response(HttpStatus.BAD_REQUEST, data);
    }

    public static ResponseEntity<Object> authError(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("status", false);
        return response(HttpStatus.UNAUTHORIZED, data);
    }

    public static ResponseEntity<Object> successPage(Page<?> page, String propertyName, String message) {

        PageObject pageable = new PageObject();
        pageable.setPage(page.getNumber());
        pageable.setSize(page.getSize());
        pageable.setTotalElements(page.getTotalElements());
        pageable.setTotalPages(page.getTotalPages());
        pageable.setLast(page.isLast());

        PageResponse resPage = new PageResponse();
        resPage.setStatus(true);
        resPage.setMessage(message);
        resPage.setPage(pageable);
        resPage.setData(page.getContent());
        resPage.setFieldName(propertyName);

        return response(HttpStatus.OK, resPage);
    }

    public static ResponseEntity<Object> successListWithSummary(List<?> list, BigDecimal summary, String propertyName, String message) {

        PageSummaryResponse resPage = new PageSummaryResponse();
        resPage.setStatus(true);
        resPage.setMessage(message);
        resPage.setData(list);
        resPage.setFieldName(propertyName);
        resPage.setSummary(summary);

        return response(HttpStatus.OK, resPage);
    }

}
