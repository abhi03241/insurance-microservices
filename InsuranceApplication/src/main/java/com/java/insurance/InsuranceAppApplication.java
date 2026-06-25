package com.java.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
@EnableMethodSecurity
@EnableTransactionManagement
@EnableConfigurationProperties
public class InsuranceAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsuranceAppApplication.class, args);
    }
}
