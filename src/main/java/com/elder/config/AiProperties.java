package com.elder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** AI service configuration. Secrets are read only from environment variables. */
@Component
public class AiProperties {

    @Value("${ai.spark.app-id:}")
    private String appId;

    @Value("${ai.spark.api-key:}")
    private String apiKey;

    @Value("${ai.spark.api-secret:}")
    private String apiSecret;

    @Value("${ai.spark.api-password:}")
    private String apiPassword;

    @Value("${ai.spark.api-version:V4_0}")
    private String apiVersion;

    @Value("${ai.spark.http-endpoint:https://spark-api-open.xf-yun.com/v2/chat/completions}")
    private String httpEndpoint;

    @Value("${ai.spark.model:x2}")
    private String model;

    @Value("${ai.spark.auth-mode:hmac}")
    private String authMode;

    public String getAppId() { return appId; }
    public String getApiKey() { return apiKey; }
    public String getApiSecret() { return apiSecret; }
    public String getApiPassword() { return apiPassword; }
    public String getApiVersion() { return apiVersion; }
    public String getHttpEndpoint() { return httpEndpoint; }
    public String getModel() { return model; }
    public String getAuthMode() { return authMode; }

    public boolean sparkConfigured() {
        if ("bearer".equalsIgnoreCase(authMode)) return hasText(apiPassword);
        return hasText(appId) && hasText(apiKey) && hasText(apiSecret);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
