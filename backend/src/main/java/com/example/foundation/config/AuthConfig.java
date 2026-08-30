package com.example.foundation.config;

import com.example.foundation.modules.auth.filter.AuthFilter;
import com.example.foundation.modules.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class AuthConfig {

    @Bean
    AuthFilter authFilter(AuthService authService, ObjectMapper objectMapper) {
        return new AuthFilter(authService, objectMapper);
    }

    @Bean
    FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }
}
