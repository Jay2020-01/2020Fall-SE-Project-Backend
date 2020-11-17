package com.backend.user.service;

import com.backend.common.entity.User;
import com.backend.user.dao.FollowDao;
import com.backend.user.dao.UserDao;
import com.backend.common.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FollowService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FollowDao followDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    public void addFollowing(Integer followingId) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(username);

        if(followDao.findConnection(user.getUserId(),followingId) != null) {
            throw new RuntimeException("关注重复");
        }

        followDao.addConnection(user.getUserId(),followingId,new Date());
    }

    public void removeFollower(Integer followerId) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(username);

        followDao.removeConnection(user.getUserId(),followerId);
    }

    public List<User> getFollowList(){
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        List<Integer> ids = followDao.getFollowList(user.getUserId());
        List<User> users = new ArrayList<>();
        for(Integer i:ids) {
            users.add(userDao.findUserById(i));
        }
        return users;
    }

    /**
     * 查询用户粉丝数量
     */
    public Long getFollowerCountById(Integer id) throws RuntimeException {
        if(userDao.findUserById(id) == null)
            throw new RuntimeException("用户不存在");
        return followDao.getFollowerCountById(id);
    }
    /**
     * 查询用户的关注数量
     */
    public Long getFollowingCountById(Integer id) throws RuntimeException {
        if(userDao.findUserById(id) == null)
            throw new RuntimeException("用户不存在");
        return followDao.getFollowingCountById(id);
    }
}
