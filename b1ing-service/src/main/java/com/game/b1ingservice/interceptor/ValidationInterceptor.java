package com.game.b1ingservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class ValidationInterceptor implements HandlerInterceptor {

    @Override

    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Pre Handler ValidationInterceptor");
        //======= check Header
//        String transactionId = request.getHeader(TRANSACTION_ID);
//        String requestDate = request.getHeader(REQUEST_DATE);
//        String language = request.getHeader(LANGUAGE);
//        String source = request.getHeader(SOURCE);
//        String system = request.getHeader(SYSTEM);
        log.info("uri : {}", request.getRequestURI());
//        log.info("transactionId : {}", transactionId);
//        log.info("requestDate : {}", requestDate);
//        log.info("language : {}", language);
        log.info("Log header 2 : host  {} : Proto {} : Port {} : X {}",
                request.getHeader("X-Forwarded-Host"),
                request.getHeader("X-Forwarded-Proto"),
                request.getHeader("X-Forwarded-Port"),
                request.getHeader("X-Real-IP"));

        return true;
    }

}
