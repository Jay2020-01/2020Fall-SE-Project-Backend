package com.backend.server.service;

import com.backend.server.entity.Notice;
import com.backend.server.entity.User;
import com.backend.server.entity.pojo.MessageList;
import com.backend.server.utils.JwtTokenUtil;
import com.backend.server.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param  username 用户名
     * @param  password 密码
     * @return 用户名、角色、token
     */
    public Map<String, Object> login(String username, String password) throws RuntimeException {
        try {
            User dbUser = this.getUserByName(username);
            System.out.println("dbUser = " + dbUser.getUserName());
            if (null == dbUser || !password.equals(dbUser.getPassword())) {
                System.out.println("用户密码错误");
                return null;
            }
            String role = dbUser.getIsAdmin()==1 ? "ADMIN" : "USER";
            String userInfoStr = dbUser.getId() + ";;" + dbUser.getUserName() + ";;" + dbUser.getIsAdmin();
            return createToken(username,dbUser.getId(),userInfoStr,role);
        } catch (Exception e) {
            System.out.println("系统登录异常! " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 注册
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @return 用户名、角色、token
     */
    public Map<String, Object> register(String username,String email,String password) throws RuntimeException {
        if (getUserByName(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (getUserByMail(email) != null) {
            throw new RuntimeException("邮箱已使用");
        }
        User userToAdd = new User();
        userToAdd.setPassword(password);
        userToAdd.setMail(email);
        userToAdd.setUserName(username);
        userToAdd.setIsAdmin(0);
        userMapper.insert(userToAdd);
        Integer id = getUserByName(username).getId();
        String userInfoStr = id + ";;" + username + ";;" + 0;
        return createToken(username,id,userInfoStr,"USER");
    }

    /**
     * 更新用户信息
     * @param userName 用户名
     * @param familyName 姓氏
     * @param name 名字
     * @param gender 性别
     * @param occupation 职业
     * @param institution 单位
     */
    public void updateInfo(String userName,String familyName,String name,String gender,String occupation,String institution) {
        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        if(userName!=null) user.setGender(userName);
        if(familyName!=null) user.setFamilyName(familyName);
        if(name!=null) user.setName(name);
        if(gender!=null) user.setGender(gender);
        if(occupation!=null) user.setOccupation(occupation);
        if(institution!=null) user.setInstitution(institution);
        // 存在token中name更新不及时的隐患
        userMapper.updateById(user);
    }

    /**
     * 更新头像
     * @param avatarUrl 头像链接
     */
    public void updateAvatar(String avatarUrl) {
        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);
    }

    /**
     * 修改密码
     * @param newPassword 新密码
     */
    public Boolean updatePassword(String oldPassword,String newPassword) {
        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        if(!user.getPassword().equals(oldPassword)) return false;
        user.setPassword(newPassword);
        userMapper.updateById(user);
        return true;
    }



    /*=====================================================================*/
    /**
     * 生成token
     */
    public Map<String, Object> createToken(String username,Integer userId, String userInfoStr,String role) {
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(userInfoStr, randomKey);
        Map<String, Object> map = new HashMap<>();
        map.put("name", username);
        map.put("user_id", userId);
        map.put("roles", role);
        map.put("token", token);
        return map;
    }


    public User getUserById(Integer userId){
        return userMapper.selectById(userId);
    }
    public User getUserByName(String userName) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        try {
            return userMapper.selectByMap(columnMap).get(0);
        }catch (Exception e){
            return null;
        }
    }
    public User getUserByMail(String mail) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("mail", mail);
        try {
            return userMapper.selectByMap(columnMap).get(0);
        }catch (Exception e){
            return null;
        }
    }

    public List<MessageList> getUserByNotice(List<Notice> notices, Integer userId) {
        List<MessageList> messageLists = new ArrayList<>();
        for (Notice notice : notices) {
            if (notice.getNotifierId().equals(userId) || notice.getReceiverId().equals(userId)) {
                Integer targetId = null;
                if (notice.getReceiverId().equals(userId)) targetId = notice.getNotifierId();
                else targetId = notice.getReceiverId();
                boolean flag = true;
                for (MessageList message : messageLists) {
                    if (message.getId().equals(targetId)) flag = false;
                }
                if (flag) {
                    User user = userMapper.selectById(targetId);
                    MessageList messageList = new MessageList();
                    BeanUtils.copyProperties(user, messageList);
                    messageLists.add(messageList);
                }
            }
        }
        return messageLists;
    }

    public void bindPortal(Integer userId, String aid) {
        User user = userMapper.selectById(userId);
        user.setAid(aid);
        userMapper.updateById(user);
    }

    public void unBindById(String aid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("aid", aid);
        User user = userMapper.selectOne(queryWrapper);
        user.setAid("");
        userMapper.updateById(user);
    }
}
