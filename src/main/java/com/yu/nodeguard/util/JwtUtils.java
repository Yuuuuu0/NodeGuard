package com.yu.nodeguard.util;

import com.yu.nodeguard.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    private JwtConfig jwtConfig;

    private static String secretKey;
    private static long expirationTime;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtConfig.getSecret().getBytes());
        expirationTime = jwtConfig.getExpiration();
        log.info("JWT配置初始化完成: secretKey={}, expiration={}", secretKey, expirationTime);
    }

    public static String generateToken(UserDetails userDetails, Integer tokenVersion) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("version", tokenVersion);
        return createToken(claims, userDetails.getUsername());
    }

    public static String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public static Integer getTokenVersion(String token) {
        return extractClaims(token).get("version", Integer.class);
    }

    public static Date getExpirationDate(String token) {
        return extractClaims(token).getExpiration();
    }

    public static boolean validateToken(String token, UserDetails userDetails, Integer currentVersion) {
        final String username = getUsername(token);
        final Integer tokenVersion = getTokenVersion(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenVersion.equals(
            currentVersion));
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    private static Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            log.error("解析token失败: token={}, error={}", token, e.getMessage());
            throw e;
        }
    }

    private static boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
