package com.backend.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.backend.common"})
public class BackendSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSearchApplication.class, args);
    }

}
