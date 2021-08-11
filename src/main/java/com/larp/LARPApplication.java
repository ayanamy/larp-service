package com.larp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LARPApplication {

    public static void main(String[] args) {
        SpringApplication.run(LARPApplication.class, args);
    }
}
