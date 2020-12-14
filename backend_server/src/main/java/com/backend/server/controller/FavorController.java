package com.backend.server.controller;

import com.backend.server.entity.Paper;
import com.backend.server.entity.User;
import com.backend.server.entity.pojo.Result;
import com.backend.server.entity.pojo.StatusCode;
import com.backend.server.service.FavorService;
import com.backend.server.service.UserService;
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
        User user = userService.getUserById(jwtTokenUtil.getUserIdFromRequest(request));
        try {
            favorService.removeFavor(paper_id,user.getId());
            return Result.create(StatusCode.OK,"取消收藏成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @GetMapping("/isFavor/{paperId}")
    public Result isFavor(@PathVariable String paperId){
        List<Paper> paperList = favorService.getFavorList();
        for(Paper p: paperList){
            if(p.getPid().equals(paperId))
                return Result.create(StatusCode.OK, "查询成功", true);
        }
        return Result.create(StatusCode.OK, "查询成功", false);
    }
}
