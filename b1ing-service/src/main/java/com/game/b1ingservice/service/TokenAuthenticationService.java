package com.game.b1ingservice.service;


import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenAuthenticationService {

    @Value("#{servletContext.contextPath}")
    private String servletContextPath;

    private final JwtTokenUtil jwtTokenUtil;
    static final String HEADER_STRING = "Authorization";
    static final String HEADER_TOKEN = "Bearer ";
    static final String QUERY_STRING_X = "/api/v1/downloadFile";

    private final AdminService adminService;


    public Authentication getAuthentication(HttpServletRequest req) {
        String token = getJwtFromRequest(req);
        if (token == null) {
            return null;
        }

        Long userId = Long.valueOf(jwtTokenUtil.getDataToken(token, "userId"));

        String username = jwtTokenUtil.getDataToken(token, "username");

        String agentIdString = jwtTokenUtil.getDataToken(token, "agentId");

        Long agentId = StringUtils.isEmpty(agentIdString)?null:Long.valueOf(agentIdString);

        String type = jwtTokenUtil.getDataToken(token, "type");

        String prefix = jwtTokenUtil.getDataToken(token, "prefix");


        UserPrincipal principal = new UserPrincipal(userId, username, "", agentId, type,null);
        principal.setPrefix(prefix);

        boolean checkAdmin = adminService.checkAdmin(userId, prefix);

        return checkAdmin && userId != null ?
                new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList())
                : null;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_TOKEN)) {
            return bearerToken.substring(6);
        }
        return null;
    }
}

