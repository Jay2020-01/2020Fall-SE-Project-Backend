package com.backend.server.controller;

import com.backend.server.entity.pojo.Message;
import com.backend.server.entity.pojo.Result;
import com.backend.server.service.AppealService;
import com.backend.server.service.NoticeService;
import com.backend.server.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppealController {
    @Autowired
    private AppealService appealService;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    //申诉请求
    //发起申诉


    //审核申诉
    @PostMapping("/review_appeal")
    public Result reviewAppeal(@Param("appeal_id") Integer appeal_id, @Param("attitude") Boolean attitude) {
        Integer userId = appealService.getAppealById(appeal_id);
        String receiverName = userService.getUserById(userId).getUserName();
        Message message = new Message();
        message.setTarget_user_id(userId);
        if (attitude) message.setContent("同意");
        else message.setContent("拒绝");
        noticeService.sendMessage(message, 0, "管理员", receiverName, 2);
        return Result.create(200, "success");
    }
}
