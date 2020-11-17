package com.backend.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.backend.common"})
public class BackendNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendNoticeApplication.class, args);
    }

}
