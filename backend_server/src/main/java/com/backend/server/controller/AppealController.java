package com.backend.server.controller;

import com.backend.server.entity.pojo.Message;
import com.backend.server.entity.pojo.PostAppeal;
import com.backend.server.entity.pojo.Result;
import com.backend.server.service.AppealService;
import com.backend.server.service.NoticeService;
import com.backend.server.service.UserService;
import com.backend.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/portal")
public class AppealController {
    @Autowired
    private AppealService appealService;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    private JwtTokenUtil util = new JwtTokenUtil();


    //发起申诉
    @PostMapping("/post_appeal")
    public Result postAppeal(@RequestBody PostAppeal appeal, HttpServletRequest request) {
        //Integer userId = util.getUserIdFromRequest(request);
        Integer userId = 1;
        appealService.insertAppeal(appeal, userId);
        return Result.create(200, "success");
    }



    //审核申诉
    @PostMapping("/review_appeal")
    public Result reviewAppeal(Integer appeal_id, Boolean attitude) {
        Integer userId = appealService.getAppealById(appeal_id);
        String receiverName = userService.getUserById(userId).getUserName();
        String content = null;
        if (attitude) content = "同意";
        else content = "拒绝";
        noticeService.sendMessage(content, userId, 0, "管理员", receiverName, 2);
        appealService.updateIsDeal(appeal_id);
        return Result.create(200, "success");
    }
}
