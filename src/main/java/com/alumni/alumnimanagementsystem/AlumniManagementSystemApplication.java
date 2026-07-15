package com.alumni.alumnimanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AlumniManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            AlumniManagementSystemApplication.class, args
        );
    }

    // FR1.3 — BCrypt with work factor 12 as per SRS
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}