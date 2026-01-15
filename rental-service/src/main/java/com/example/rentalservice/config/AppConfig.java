package com.example.rentalservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced; // <-- HADI LI NAQSAK
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}