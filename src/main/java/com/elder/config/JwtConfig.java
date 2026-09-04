package com.elder.config;

import com.elder.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${JWT_SECRET:}")
    private String secret;

    @PostConstruct
    void configureJwt() {
        JwtUtil.configure(secret);
    }
}
