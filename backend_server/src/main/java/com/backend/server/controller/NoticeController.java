package com.backend.server.controller;

import com.backend.server.entity.Notice;
import com.backend.server.entity.pojo.MessageList;
import com.backend.server.entity.pojo.Result;
import com.backend.server.service.AuthorService;
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

    @Autowired
    private AuthorService authorService;

    @Autowired
    private JwtTokenUtil util;

    //私信

    /**
     * 拉取私信列表
     * @param request
     * @return
     */
    @GetMapping("/get_person_list/{user_id}")
    public Result getMessageList(@PathVariable("user_id") Integer userId, HttpServletRequest request) {
        //Integer userId = util.getUserIdFromRequest(request);
        //Integer userId = 18;
        List<Notice> notices = noticeService.getMessageByUserId(userId);
        List<MessageList> personList = userService.getUserByNotice(notices, userId);
        authorService.getNameByUserId(personList);

        return Result.create(200, "success", personList);
    }


    @GetMapping("/get_message_content/{user_id}")
    public Result getMessageContent(@PathVariable("user_id") Integer userId, @RequestParam("target_user_id")Integer target, HttpServletRequest request) {
        //Integer userId = util.getUserIdFromRequest(request);
        //Integer userId = 18;
        List<Notice> notices = noticeService.getMessageByIds(userId, target);
        return Result.create(200, "success", notices);
    }

    //发送私信
    @PostMapping("/post_message/{user_id}")
    public Result sendMessage(@PathVariable("user_id") Integer userId, String content, Integer target_user_id,HttpServletRequest request) {
        //Integer userId = util.getUserIdFromRequest(request);
        //Integer userId = 18;
        String notifierName = userService.getUserById(userId).getUserName();
        String receiverName = userService.getUserById(target_user_id).getUserName();
        noticeService.sendMessage(content, target_user_id, userId, notifierName, receiverName, 1);
        return Result.create(200, "success");
    }

}
