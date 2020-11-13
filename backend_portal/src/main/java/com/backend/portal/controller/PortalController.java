package com.backend.portal.controller;

import com.backend.common.entity.Result;
import com.backend.portal.entity.Portal;
import com.backend.portal.service.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal")
public class PortalController {
    @Autowired
    private PortalService portalService;

    @GetMapping("/test")
    public Result test() {
        Portal portal = portalService.testController();
        return Result.create(200, "success", portal);
    }

    @GetMapping("/view")
    public Result viewPortal() {

        return Result.create(200, "success");
    }

    @PostMapping("/modify")
    public Result modifyPortal() {

        return Result.create(200, "success");
    }

    @PostMapping("/certification")
    public Result certificationPortal() {

        return Result.create(200, "success");
    }
}
