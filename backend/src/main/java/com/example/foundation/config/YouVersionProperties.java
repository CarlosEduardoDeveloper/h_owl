package com.example.foundation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.youversion")
public record YouVersionProperties(
        String baseUrl,
        String appKey,
        int connectTimeoutMs,
        int readTimeoutMs
) {

    public boolean isConfigured() {
        return appKey != null && !appKey.isBlank();
    }
}
