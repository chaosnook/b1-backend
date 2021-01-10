package com.game.b1ingservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Map<String, Object> claims, String subject) {
        return doGenerateToken(claims, subject);
    }

    public String getDataToken(String token, String key) {
        final Claims claims = getAllClaimsFromToken(token);
        return String.valueOf(claims.get(key));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> claims, String subject, int expireMinute) {
        final Date createdDate = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(new Date(System.currentTimeMillis() + expireMinute * 60 * 1000 ))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "jwt");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setHeader(header)
//                .setExpiration(expirationDate)
                .signWith(
                        SignatureAlgorithm.HS512,
                        secret
                )
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}
