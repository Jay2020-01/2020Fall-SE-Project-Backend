package com.backend.server.controller;

import com.backend.server.entity.Notice;
import com.backend.server.entity.pojo.Message;
import com.backend.server.entity.pojo.MessageList;
import com.backend.server.entity.pojo.Result;
import com.backend.server.service.NoticeService;
import com.backend.server.service.UserService;
import com.backend.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/notice")
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
        //Integer userId = 1;
        List<Notice> notices = noticeService.getMessageByUserId(userId);
        List<MessageList> personList = userService.getUserByNotice(notices, userId);
        return Result.create(200, "success", personList);
    }


    @GetMapping("/get_message_content")
    public Result getMessageContent(@RequestParam("target_user_id")Integer target, HttpServletRequest request) {
        Integer userId = util.getUserIdFromRequest(request);
        //Integer userId = 1;
        List<Notice> notices = noticeService.getMessageByIds(userId, target);

        return Result.create(200, "success", notices);
    }

    //发送私信
    @PostMapping("/post_message")
    public Result sendMessage(String content, Integer target_user_id,HttpServletRequest request) {
        JwtTokenUtil util = new JwtTokenUtil();
        Integer userId = util.getUserIdFromRequest(request);
        //Integer userId = 1;
        String notifierName = userService.getUserById(userId).getUserName();
        String receiverName = userService.getUserById(target_user_id).getUserName();
        noticeService.sendMessage(content, target_user_id, userId, notifierName, receiverName, 1);
        return Result.create(200, "success");
    }

}
