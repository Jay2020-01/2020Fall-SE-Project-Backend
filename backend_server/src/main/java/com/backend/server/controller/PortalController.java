package com.backend.server.controller;

import com.backend.server.entity.pojo.*;
import com.backend.server.entity.Portal;
import com.backend.server.service.PortalService;
import com.backend.server.utils.FormatUtil;
import com.backend.server.utils.JwtTokenUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PortalController {
    @Autowired
    private PortalService portalService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;



    @PostMapping("/personal_center/academic_homepage/view")
    public Result checkPortal(@Param("id") Integer portalId) {
        Portal portal = portalService.selectById(portalId);
        if (portal == null) return Result.create(StatusCode.NOTFOUND, "门户不存在");
        PortalPage portalPage = new PortalPage();
        BeanUtils.copyProperties(portal, portalPage);
        //其他相关信息

        return Result.create(200, "success", portalPage);
    }


    @PostMapping("/profile/view")
    public Result viewPortal(@Param("id") Integer portalId) {
        PortalInfo portalInfo = new PortalInfo();
        Portal portal = portalService.selectById(portalId);
        if (portal == null) return Result.create(StatusCode.NOTFOUND, "门户不存在");
        PortalPage portalPage = new PortalPage();
        BeanUtils.copyProperties(portal, portalPage);
        //其他相关信息


        return Result.create(200, "success", portalInfo);
    }

    @PostMapping("/profile/modify")
    public Result modifyPortal(@RequestBody Portal portal) {
        int result = portalService.updateById(portal);
        if (result == 0) return Result.create(StatusCode.ERROR, "更新失败");
        return Result.create(200, "success");
    }


    //绑定
    @PostMapping("/personal_center/academic_homepage/bind")
    public Result certificationPortal(@RequestBody Certification certification, HttpServletRequest request) {
        //测试姓名、邮箱基本信息是否符合
        //boolean isOwner = portalService.checkInformation(certification);
        //if (!isOwner) return Result.create(StatusCode.INFORMATION_ERROR, "您的信息不符合认证标准");
        //查看验证码是否正确
        //boolean isTrue = portalService.checkMailCode(certification.getEmail(), certification.getCode());
        //if (!isTrue) return Result.create(StatusCode.CODE_ERROR, "验证码错误");
        //成功关联用户与门户
        JwtTokenUtil util = new JwtTokenUtil();
        Integer userId = util.getUserIdFromRequest(request);
        portalService.relateUserToPortal(certification, userId);
        return Result.create(200, "success");
    }

    @PostMapping("/sendmail")
    public Result sendmail(String mail){
        if (!FormatUtil.checkMail(mail)) return Result.create(StatusCode.INFORMATION_ERROR, "邮箱格式错误");
        String redisMailCode = redisTemplate.opsForValue().get("MAIL_"+mail);
        if(redisMailCode!=null){
            return Result.create(200,"请稍后再发送");
        }else{
            portalService.sendMail(mail);
            return Result.create(200,"发送成功");
        }
    }
}
