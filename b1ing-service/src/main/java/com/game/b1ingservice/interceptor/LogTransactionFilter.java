package com.game.b1ingservice.interceptor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@EqualsAndHashCode(callSuper=false)
@Component
public class LogTransactionFilter extends OncePerRequestFilter {

    final static String mdcTransactionId = "TransactionId";
    final static String mdcSource = "Source";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put(mdcTransactionId, request.getHeader("TransactionId"));
            MDC.put(mdcSource, request.getHeader("Source"));

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(mdcTransactionId);
            MDC.remove(mdcSource);

        }
    }
}
