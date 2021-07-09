package com.game.b1ingservice.interceptor;

import com.game.b1ingservice.service.TokenAuthenticationService;
import com.game.b1ingservice.utils.MultiReadRequest;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
@Slf4j
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final Collection<String> excludeUrlPatterns = new ArrayList<>(
            Arrays.asList(
                    "/api/admin/auth",
                    "/api/user/auth",
                    "/api/user/register",
                    "/api/file/downloadFile/*",
                    "/api/master/verifyTel"
            ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (request.getContentType() != null && request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            try {
                Authentication authentication = tokenAuthenticationService.getAuthentication(request);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore in header", e);
            } catch (MalformedJwtException e) {
                logger.warn(e.getMessage(), e);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            filterChain.doFilter(request, response);
        } else {
            MultiReadRequest multiReadRequest = new MultiReadRequest(request);
            log.info("Log header : host  {} : Proto {} : Port {}", multiReadRequest.getHeader("X-Forwarded-Host"),
                    multiReadRequest.getHeader("X-Forwarded-Proto"),
                    multiReadRequest.getHeader("X-Forwarded-Port"));
            log.info("Before Filter ");

            try {
                Authentication authentication = tokenAuthenticationService.getAuthentication(multiReadRequest);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore in header", e);
            } catch (MalformedJwtException e) {
                logger.warn(e.getMessage(), e);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            filterChain.doFilter(multiReadRequest, response);
        }


        log.info("After Filter");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath())) || !request.getServletPath().startsWith("/api");
    }

}
