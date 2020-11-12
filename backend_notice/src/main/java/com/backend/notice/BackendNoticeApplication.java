package com.backend.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BackendNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendNoticeApplication.class, args);
    }

}
