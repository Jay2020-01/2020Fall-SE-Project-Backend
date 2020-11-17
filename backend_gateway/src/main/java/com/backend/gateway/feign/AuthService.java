package com.backend.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "backend-user")
public interface AuthService {

    /**
     * token 校验
     */
    @RequestMapping(value = { "/user/checkToken" }, method = RequestMethod.POST)
    public int checkToken(@RequestParam(value = "authToken") String authToken);

}
