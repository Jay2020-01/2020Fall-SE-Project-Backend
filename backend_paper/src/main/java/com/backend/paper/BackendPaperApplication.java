package com.backend.paper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BackendPaperApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPaperApplication.class, args);
    }

}
