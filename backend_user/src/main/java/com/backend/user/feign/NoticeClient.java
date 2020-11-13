package com.backend.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("backend-notice")
public interface NoticeClient {

    @RequestMapping("message/hello")
    public String hello();
}
