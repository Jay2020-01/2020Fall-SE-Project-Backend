package com.backend.notice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("message")
public class MessageController {

    @GetMapping("/hello")
    public String hello(){
//        int i = 1/0;
        return "hello notice";
    }
}
