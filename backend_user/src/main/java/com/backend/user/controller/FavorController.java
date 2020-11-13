package com.backend.user.controller;

import com.backend.common.entity.*;
import com.backend.user.dao.UserDao;
import com.backend.user.feign.NoticeClient;
import com.backend.user.service.FavorService;
import com.backend.user.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("favor")
public class FavorController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FavorService favorService;
    @Autowired
    private NoticeClient noticeClient;

    @GetMapping("/hi")
    public String hi(){
        return this.noticeClient.hello();
    }

    @GetMapping("/getFavor")
    public Result getFavorList(){
        try {
            List<Paper> paperList = favorService.getFavorList();
            return Result.create(StatusCode.OK, "获取成功",paperList);
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @PostMapping("/add/{paperId}")
    public Result addFavor(@PathVariable Integer paperId){
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        try {
            if(favorService.isFavored(paperId,user.getUserId()))
                return Result.create(StatusCode.OK,"学术成果已收藏");
            favorService.addFavor(paperId,user.getUserId());
            return Result.create(StatusCode.OK,"收藏成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @PostMapping("/remove/{paperId}")
    public Result removeFavor(@PathVariable Integer paperId){
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        try {
            if(!favorService.isFavored(paperId,user.getUserId()))
                return Result.create(StatusCode.OK,"学术成果未收藏");
            favorService.removeFavor(paperId,user.getUserId());
            return Result.create(StatusCode.OK,"取消收藏成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

}
