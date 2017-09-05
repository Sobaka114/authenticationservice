package com.sasha.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

@ComponentScan(basePackages = "com.sasha.authenticationservice")
public class AuthenticationServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApp.class, args);
    }
}
