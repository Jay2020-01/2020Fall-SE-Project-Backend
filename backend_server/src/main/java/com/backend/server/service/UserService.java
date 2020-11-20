package com.backend.server.service;

import com.backend.server.entity.User;
import com.backend.server.utils.JwtTokenUtil;
import com.backend.server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
     * @param user 用户名、密码
     * @return 用户名、角色、token
     */
    public Map<String, Object> login(User user) throws RuntimeException {
        try {
            User dbUser = this.getUserByName(user.getUserName());
            System.out.println("dbUser = " + dbUser);
            if (null == dbUser || !user.getPassword().equals(dbUser.getPassword())) {
                System.out.println("用户密码错误");
                throw new RuntimeException("用户名或密码错误");
            }
            String role = dbUser.getIsAdmin()==1 ? "ADMIN" : "USER";
            String userInfoStr = dbUser.getId() + ";;" + dbUser.getUserName() + ";;" + dbUser.getIsAdmin();
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(userInfoStr, randomKey);
            System.out.println("token = " + token);
            Map<String, Object> map = new HashMap<>();
            map.put("name", user.getUserName());
            map.put("roles", role);
            map.put("token", token);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("系统异常! " + e);
            return null;
        }
    }

    /**
     * 注册
     * @param name 用户名
     * @param password 密码
     * @param mail 邮箱
     * @param mailCode 验证码
     */
    public void register(String name,String password,String mail, String mailCode) throws RuntimeException {

        if (!checkMailCode(mail, mailCode)) {
            throw new RuntimeException("验证码错误");
        }
        if (getUserByName(name) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (getUserByMail(mail) != null) {
            throw new RuntimeException("邮箱已使用");
        }
        User userToAdd = new User();
        userToAdd.setPassword(password);
        userToAdd.setMail(mail);
        userToAdd.setUserName(name);
        userMapper.insert(userToAdd);

    }

    /**
     * 更新用户信息
     * @param gender 性别
     * @param phone 手机号
     * @param major 专业
     * @param campus 大学
     * @param institution 机构
     */
    public void updateInfo(String gender, String phone,String major, String campus, String institution) {
        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        if(gender!=null) user.setGender(gender);
        if(phone!=null) user.setPhone(phone);
        if(major!=null) user.setMajor(major);
        if(campus!=null) user.setCampus(campus);
        if(institution!=null) user.setInstitution(institution);
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
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void updatePassword(String oldPassword, String newPassword) {

        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }



    /*=====================================================================*/

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
}
