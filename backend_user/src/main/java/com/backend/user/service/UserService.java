package com.backend.user.service;

import com.backend.common.entity.User;
import com.backend.common.utils.JwtTokenUtil;
import com.backend.user.dao.UserDao;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//@Slf4j
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 登录
     * 返回token，用户名，用户角色
     */
    public Map<String, Object> login(User user) throws RuntimeException {
        try {
            User dbUser = this.findUserByName(user.getName());
            System.out.println("dbUser = " + dbUser);
            if (null == dbUser || !user.getPassword().equals(dbUser.getPassword())) {
                System.out.println("用户密码错误");
                throw new RuntimeException("用户名或密码错误");
            }
            String role = dbUser.getIsAdmin()==1 ? "ADMIN" : "USER";
            String userInfoStr = dbUser.getUserId() + ";;" + dbUser.getName() + ";;" + dbUser.getIsAdmin();
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(userInfoStr, randomKey);
            System.out.println("token = " + token);
            Map<String, Object> map = new HashMap<>();
            map.put("name", user.getName());
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
     */
    public void register(String name,String password,String mail, String mailCode) throws RuntimeException {

        if (!checkMailCode(mail, mailCode)) {
            throw new RuntimeException("验证码错误");
        }
        if (userDao.findUserByName(name) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (userDao.findUserByMail(mail) != null) {
            throw new RuntimeException("邮箱已使用");
        }
        User userToAdd = new User();
        userToAdd.setPassword(password);
        userToAdd.setMail(mail);
        userToAdd.setName(name);
        userDao.saveUser(userToAdd);

    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(String gender, String phone,String major, String campus, String institution) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        if(gender!=null) user.setGender(gender);
        if(phone!=null) user.setPhone(phone);
        if(major!=null) user.setMajor(major);
        if(campus!=null) user.setCampus(campus);
        if(institution!=null) user.setInstitution(institution);
        userDao.updateUser(user);
    }
    /**
     * 修改用户密码
     */
    public void updateUserPassword(String oldPassword, String newPassword) {

        String name = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(name);
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        user.setPassword(newPassword);
        userDao.updateUser(user);
    }

    /**
     * 将邮件和验证码发送到消息队列
     */
    public void sendMail(String mail) {
        Random r = new Random();
        int random = r.nextInt(899999) + 100001;
        Map<String, String> map = new HashMap<>();
        String code = Integer.toString(random);
        map.put("mail", mail);
        map.put("code", code);

        //保存发送记录
        redisTemplate.opsForValue().set("MAIL_" + mail, code, 1, TimeUnit.MINUTES);
        rabbitTemplate.convertAndSend("MAIL", map);
    }

    /*=====================================================================*/

    /**
     * 从redis中提取 验证码
     */
    public String getMailCodeFromRedis(String mail) {
        return redisTemplate.opsForValue().get("MAIL_" + mail);
    }

    /**
     * 校验验证码是否正确
     */
    public boolean checkMailCode(String mail, String code) {
        String mailCode = getMailCodeFromRedis(mail);
        return code.equals(mailCode);
    }

    public User getUserInfo(){
        return userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
    }
    public User getUserInfoByName(String userName){
        return userDao.findUserByName(userName);
    }
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }

}
