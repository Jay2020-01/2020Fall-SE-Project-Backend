package com.backend.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.backend.portal.mapper")
@ServletComponentScan
public class BackendPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPortalApplication.class, args);
    }

}
