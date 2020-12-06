package com.backend.server.controller;

import com.backend.server.entity.Notice;
import com.backend.server.entity.pojo.Message;
import com.backend.server.entity.pojo.MessageList;
import com.backend.server.entity.pojo.Result;
import com.backend.server.service.NoticeService;
import com.backend.server.service.UserService;
import com.backend.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    private JwtTokenUtil util = new JwtTokenUtil();

    //私信

    /**
     * 拉取私信列表
     * @param request
     * @return
     */
    @GetMapping("/get_person_list")
    public Result getMessageList(HttpServletRequest request) {
        Integer userId = util.getUserIdFromRequest(request);
        List<Notice> notices = noticeService.getMessageByUserId(userId);
        List<MessageList> personList = userService.getUserByNotice(notices, userId);
        return Result.create(200, "success", personList);
    }


    @GetMapping("/get_message_content")
    public Result getMessageContent(HttpServletRequest request) {
        Integer userId = util.getUserIdFromRequest(request);
        List<Notice> notices = noticeService.getMessageByUserId(userId);

        return Result.create(200, "success");
    }

    //发送私信
    @PostMapping("/post_message")
    public Result sendMessage(@RequestBody Message message, HttpServletRequest request) {
        JwtTokenUtil util = new JwtTokenUtil();
        Integer userId = util.getUserIdFromRequest(request);
        String notifierName = userService.getUserById(userId).getUserName();
        String receiverName = userService.getUserById(message.getTarget_user_id()).getUserName();
        noticeService.sendMessage(message, userId, notifierName, receiverName, 1);
        return Result.create(200, "success");
    }


}
