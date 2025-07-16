package com.project.course.backend.module.auth.component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtComponent {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-days}")
    private Integer accessExpirationDays;

    @Value("${security.jwt.access-token-expiration-hours}")
    private Integer accessExpirationHours;

    @Value("${security.jwt.access-token-expiration-minutes}")
    private Integer accessExpirationMinutes;

    @Value("${security.jwt.refresh-token-expiration-days}")
    private Integer refreshExpirationDays;

    @Value("${security.jwt.refresh-token-expiration-hours}")
    private Integer refreshExpirationHours;

    @Value("${security.jwt.refresh-token-expiration-minutes}")
    private Integer refreshExpirationMinutes;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String buildToken(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .issuer("Course Backend")
                .subject("Authentication Token")
                .signWith(getSecretKey(), Jwts.SIG.HS512)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claims(claims)
                .compact();
    }

    public String generateAccessToken(Map<String, Object> claims) {
        return buildToken(claims,
                (24 * 60 * 60 * 1000L * accessExpirationDays) +
                        (60 * 60 * 1000L * accessExpirationHours) +
                        (60 * 1000L * accessExpirationMinutes));
    }

    public String generateRefreshToken(Map<String, Object> claims) {
        return buildToken(claims,
                (24 * 60 * 60 * 1000L * refreshExpirationDays) +
                        (60 * 60 * 1000L * refreshExpirationHours) +
                        (60 * 1000L * refreshExpirationMinutes));
    }

    public Map<String, Object> extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("x-refresh-token", refreshToken);
        refreshTokenCookie.setHttpOnly(true); // ป้องกันการ access ผ่าน JavaScript
        refreshTokenCookie.setSecure(true);   // ส่ง cookie เฉพาะเมื่อใช้ HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(
                (refreshExpirationDays * 24 * 60 * 60 * 1000) +
                        (refreshExpirationHours * 60 * 60 * 1000) +
                        (refreshExpirationMinutes * 60 * 1000));

        response.addCookie(refreshTokenCookie);
    }


}
