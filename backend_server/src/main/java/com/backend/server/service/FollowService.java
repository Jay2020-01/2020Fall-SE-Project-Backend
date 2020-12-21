package com.backend.server.service;

import com.backend.server.dao.AuthorDaoImp;
import com.backend.server.entity.Author;
import com.backend.server.entity.Follow;
import com.backend.server.entity.User;
import com.backend.server.utils.JwtTokenUtil;
import com.backend.server.mapper.FollowMapper;
import com.backend.server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class FollowService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthorDaoImp authorDaoImp;
    @Autowired
    private HttpServletRequest request;

    /**
     * 添加关注
     * @param followingId 被关注者id
     */
    public void addFollowing(Integer followerId, String followingId) {
        Follow follow = new Follow(null,followerId,followingId,new Date());
        followMapper.insert(follow);
    }

    /**
     * 移除关注
     * @param followingId 被关注着id
     */
    public void removeFollower(String followingId) {
        User user = userService.getUserById(jwtTokenUtil.getUserIdFromRequest(request));
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("follower_id",user.getId());
        columnMap.put("following_id", followingId);
        followMapper.deleteByMap(columnMap);
    }

    /**
     * 获取当前用户关注列表
     */
    public List<Author> getFollowList(){
        User user = userService.getUserById(jwtTokenUtil.getUserIdFromRequest(request));
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("follower_id",user.getId());
        List<Follow> ids = followMapper.selectByMap(columnMap);
        System.out.println("ids = " + ids);
        List<Author> authors = new ArrayList<>();
        for(Follow f:ids) {
            authors.add(authorDaoImp.findAuthorById(f.getFollowingId()));
        }
        return authors;
    }

    /**
     * 查询用户粉丝数量
     * @param id 用户id
     */
    public int getFollowerCountById(String id) throws RuntimeException {
//        if(userMapper.selectById(id) == null)
//            throw new RuntimeException("用户不存在");
        System.out.println("following_id = " + id);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("following_id",id);
        return followMapper.selectByMap(columnMap).size();
    }

    /**
     * 查询用户的关注数量
     * @param id 用户id
     */
    public int getFollowingCountById(Integer id) throws RuntimeException {
        if(userMapper.selectById(id) == null)
            throw new RuntimeException("用户不存在");
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("follower_id",id);
        return followMapper.selectByMap(columnMap).size();
    }

    /**
     * 关注是否已存在
     * @param followerId 关注者id
     * @param followingId 被关注着id
     */
    public boolean isFollowed(Integer followerId,String followingId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("follower_id",followerId);
        columnMap.put("following_id", followingId);
        List<Follow> followList = followMapper.selectByMap(columnMap);
        return followList.size() == 1;
    }
}
