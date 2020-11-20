package com.backend.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
public class BackendSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSearchApplication.class, args);
    }

}
