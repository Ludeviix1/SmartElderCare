package com.elder.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public final class JwtUtil {

    private static volatile String key;

    private JwtUtil() {
    }

    public static void configure(String secret) {
        if (secret == null || secret.trim().length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be configured with at least 32 characters");
        }
        key = secret.trim();
    }

    public static String createToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(key()));
    }

    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(key()))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

    private static String key() {
        if (key == null) {
            throw new IllegalStateException("JWT secret has not been configured");
        }
        return key;
    }
}
