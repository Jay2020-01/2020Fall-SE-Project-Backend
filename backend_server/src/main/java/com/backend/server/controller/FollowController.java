package com.backend.server.controller;

import com.backend.server.entity.pojo.PageResult;
import com.backend.server.entity.pojo.Result;
import com.backend.server.entity.pojo.StatusCode;
import com.backend.server.entity.User;
import com.backend.server.service.FollowService;
import com.backend.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;

    /**
     * 关注用户
     */
    @PostMapping("/follow_scholar")
    public Result newFollow(String person_id) {
        Integer followingId  = userService.getUserById(Integer.parseInt(person_id)).getId();
        try {
            followService.addFollowing(followingId);
            return Result.create(StatusCode.OK, "关注成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    /**
     * 移除关注
     */
    @DeleteMapping("/remove/{userName}")
    public Result removeFollow(@PathVariable String userName) {
        Integer followingId  = userService.getUserByName(userName).getId();
        try{
            followService.removeFollower(followingId);
            return Result.create(StatusCode.OK, "移除成功");
        }catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    /**
     * 查询关注列表 TODO
     */
    @GetMapping("/myFollowing")
    public Result getFollowList() {

        List<User> res = followService.getFollowList();
        PageResult<User> result = new PageResult<>((long) res.size(),res);

        return Result.create(StatusCode.OK, "查询成功", result);
    }

    /**
     * 查询用户粉丝数
     */
    @GetMapping("/followingNum/{userName}")
    public Result followingNum(@PathVariable String userName){
        Integer userId = userService.getUserByName(userName).getId();
        int num = followService.getFollowingCountById(userId);
        return Result.create(StatusCode.OK, "查询成功", num);
    }

    /**
     * 查询用户关注数
     */
    @GetMapping("/followerNum/{userName}")
    public Result followerNum(@PathVariable String userName){
        Integer userId = userService.getUserByName(userName).getId();
        int num = followService.getFollowerCountById(userId);
        return Result.create(StatusCode.OK, "查询成功", num);
    }

    /**
     * 是否关注此Name用户 TODO
     */
    @GetMapping("/isFollow/{userName}")
    public Result isFollow(@PathVariable String userName){
        List<User> res = followService.getFollowList();
        for(User user: res){
            if(user.getUserName().equals(userName))
                return Result.create(StatusCode.OK, "查询成功", true);
        }
        return Result.create(StatusCode.OK, "查询成功", false);
    }
}
