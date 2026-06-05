package com.nobolso.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoBolsoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoBolsoApiApplication.class, args);
    }
}
