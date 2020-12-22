package com.backend.server.controller;

import com.backend.server.entity.Paper;
import com.backend.server.entity.User;
import com.backend.server.entity.pojo.Result;
import com.backend.server.entity.pojo.StatusCode;
import com.backend.server.service.FavorService;
import com.backend.server.service.UserService;
import com.backend.server.utils.FormatUtil;
import com.backend.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("favor")
public class FavorController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private FormatUtil formatUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FavorService favorService;
    @Autowired
    private UserService userService;

    @GetMapping("/my_collection")
    public Result getFavorList(){
        try {
            List<Paper> paperList = favorService.getFavorList();
            return Result.create(StatusCode.OK, "获取成功",paperList);
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @PostMapping("/collect_paper")
    public Result addFavor(String paper_id){
        if (!formatUtil.checkStringNull(paper_id)) {
            return Result.create(StatusCode.ERROR, "paper_id为空");
        }
        User user = userService.getUserById(jwtTokenUtil.getUserIdFromRequest(request));
        try {
            if(favorService.isFavored(paper_id,user.getId()))
                return Result.create(StatusCode.OK,"学术成果已收藏");
            favorService.addFavor(paper_id,user.getId());
            return Result.create(StatusCode.OK,"收藏成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @PostMapping("/remove_paper")
    public Result removeFavor(String paper_id){
        if (!formatUtil.checkStringNull(paper_id)) {
            return Result.create(StatusCode.ERROR, "paper_id为空");
        }
        User user = userService.getUserById(jwtTokenUtil.getUserIdFromRequest(request));
        try {
            favorService.removeFavor(paper_id,user.getId());
            return Result.create(StatusCode.OK,"取消收藏成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @GetMapping("/isFavor")
    public Result isFavor(String paper_id){
        if (!formatUtil.checkStringNull(paper_id)) {
            return Result.create(StatusCode.ERROR, "paper_id为空");
        }
        Integer userId = jwtTokenUtil.getUserIdFromRequest(request);
        return Result.create(StatusCode.OK, "查询成功", favorService.isFavored(paper_id,userId));
    }
}
