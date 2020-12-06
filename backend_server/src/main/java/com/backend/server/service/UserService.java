package com.backend.server.service;

import com.backend.server.entity.Notice;
import com.backend.server.entity.User;
import com.backend.server.entity.pojo.MessageList;
import com.backend.server.utils.JwtTokenUtil;
import com.backend.server.mapper.UserMapper;
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
            System.out.println("dbUser = " + dbUser);
            if (null == dbUser || !password.equals(dbUser.getPassword())) {
                System.out.println("用户密码错误");
                throw new RuntimeException("用户名或密码错误");
            }
            String role = dbUser.getIsAdmin()==1 ? "ADMIN" : "USER";
            String userInfoStr = dbUser.getId() + ";;" + dbUser.getUserName() + ";;" + dbUser.getIsAdmin();
            return createToken(username,userInfoStr,role);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("系统异常! " + e);
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
        // if (!checkMailCode(mail, mailCode)) {
        //     throw new RuntimeException("验证码错误");
        // }
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
        //TODO 直接自动登录？？返回TOKEN
        Integer id = getUserByName(username).getId();
        String userInfoStr = id + ";;" + username + ";;" + 0;
        return createToken(username,userInfoStr,"USER");
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
        // TODO 更新token、关注表
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
    public void updatePassword(String newPassword) {

        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }



    /*=====================================================================*/
    /**
     * 生成token
     */
    public Map<String, Object> createToken(String username, String userInfoStr,String role) {
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(userInfoStr, randomKey);
        System.out.println("token = " + token);
        Map<String, Object> map = new HashMap<>();
        map.put("name", username);
        map.put("roles", role);
        map.put("token", token);
        return map;
    }

    /**
     * 从redis中提取验证码并校验
     * @param mail 邮箱
     * @param code 验证码
     */
    public boolean checkMailCode(String mail, String code) {
        String mailCode = redisTemplate.opsForValue().get("MAIL_" + mail);
        return code.equals(mailCode);
    }

    public User getUserById(Integer userId){
        return userMapper.selectById(userId);
    }
    public User getUserByName(String userName) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        return userMapper.selectByMap(columnMap).get(0);
    }
    public User getUserByMail(String mail) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("mail", mail);
        return userMapper.selectByMap(columnMap).get(0);
    }

    public List<MessageList> getUserByNotice(List<Notice> notices, Integer userId) {
        List<MessageList> messageLists = new ArrayList<>();
        for (Notice notice : notices) {
            User user;
            MessageList messageList = new MessageList();
            if (notice.getNotifierId().equals(userId)) {
                user = userMapper.selectById(notice.getReceiverId());
                BeanUtils.copyProperties(user, messageList);
                messageList.setNotice(notice);
                messageLists.add(messageList);
            }
            else if (notice.getReceiverId().equals(userId)) {
                user = userMapper.selectById(notice.getReceiverId());
                BeanUtils.copyProperties(user, messageList);
                messageList.setNotice(notice);
                messageLists.add(messageList);
            }
        }
        return messageLists;
    }
}
