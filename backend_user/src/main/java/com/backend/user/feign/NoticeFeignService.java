package com.backend.user.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("backend_notice")
public interface NoticeFeignService {
}
