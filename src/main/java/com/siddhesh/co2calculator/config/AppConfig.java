package com.siddhesh.co2calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Creates and returns a new RestTemplate instance to be injected wherever needed in the application
        return new RestTemplate();
    }
}
