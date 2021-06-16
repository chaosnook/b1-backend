package com.game.b1ingservice.interceptor;

import com.game.b1ingservice.exception.DuplicateLoginException;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final AdminService adminService;

    static final String HEADER_STRING = "Authorization";
    static final String HEADER_TOKEN = "Bearer ";

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Pre Handler ValidationInterceptor");
        //======= check Header

        log.info("uri : {}", request.getRequestURI());

        String token = getJwtFromRequest(request);

        String prefix = "";
        Long userId = 0L;
        String type = jwtTokenUtil.getDataToken(token, "type");
        try {
            prefix = jwtTokenUtil.getDataToken(token, "prefix");
            userId = Long.valueOf(jwtTokenUtil.getDataToken(token, "userId"));
        } catch (Exception e) {
            //ignore
        }

        if (type != null) {
            boolean checkAdmin = adminService.checkAdmin(userId, prefix);
            if (checkAdmin) {
                if (!adminService.checkLastUQToken(userId, jwtTokenUtil.getDataToken(token, "uq"))) {
                    throw new DuplicateLoginException();
                }
            }
        }

        return true;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_TOKEN)) {
            return bearerToken.substring(6);
        }
        return null;
    }

}
