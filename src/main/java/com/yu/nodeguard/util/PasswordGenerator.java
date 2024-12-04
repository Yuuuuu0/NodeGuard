package com.yu.nodeguard.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin111";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后: " + encodedPassword);

        // 验证
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("验证结果: " + matches);

        // 验证数据库中的密码
        String dbPassword = "$2a$10$st1IoY/ogHWszXUzRFcOoeqnFgVfxrXURDne9b2DXyJUKEPQ1Y0cq";
        boolean dbMatches = encoder.matches(rawPassword, dbPassword);
        System.out.println("与数据库密码匹配结果: " + dbMatches);
    }
}
