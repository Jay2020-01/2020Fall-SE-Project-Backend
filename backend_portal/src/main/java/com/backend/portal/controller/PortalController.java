package com.backend.portal.controller;

import com.backend.common.entity.Result;
import com.backend.common.entity.StatusCode;
import com.backend.portal.entity.Certification;
import com.backend.portal.entity.Portal;
import com.backend.portal.service.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal")
public class PortalController {
    @Autowired
    private PortalService portalService;

    @GetMapping("/test")
    public Result test() {
        return Result.create(200, "success");
    }

    @GetMapping("/view/{portalId}")
    public Result viewPortal(@PathVariable("portalId") Integer portalId) {
        Portal portal = portalService.selectById(portalId);
        if (portal == null) return Result.create(StatusCode.NOTFOUND, "门户不存在");
        return Result.create(200, "success", portal);
    }

    @PostMapping("/modify")
    public Result modifyPortal(@RequestBody Portal portal) {
        int result = portalService.updateById(portal);
        if (result == 0) return Result.create(StatusCode.ERROR, "更新失败");
        return Result.create(200, "success");
    }


    @PostMapping("/certification")
    public Result certificationPortal(@RequestBody Certification certification) {
        //测试姓名、邮箱基本信息是否符合
        boolean isOwner = portalService.checkInformation(certification);
        if (!isOwner) return Result.create(StatusCode.INFORMATION_ERROR, "您的信息不符合认证标准");
        //发送邮件测试

        //成功关联用户与门户
        portalService.relateUserToPortal(certification);
        return Result.create(200, "success");
    }
}
