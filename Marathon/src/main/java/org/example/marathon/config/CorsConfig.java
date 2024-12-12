package org.example.marathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {
    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许的来源
        corsConfiguration.addAllowedOrigin("http://localhost:8081");
        // 或使用通配符
        // corsConfiguration.addAllowedOriginPattern("http://*.example.com");

        // 允许携带凭证
        corsConfiguration.setAllowCredentials(true);

        // 允许的请求方法和头部
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new org.springframework.web.filter.CorsFilter(source);
    }
}

