package com.backend.user.service;

import com.backend.common.entity.User;
import com.backend.user.config.JwtConfig;
import com.backend.user.dao.UserDao;
import com.backend.user.utils.JwtTokenUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtConfig jwtConfig;
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

        User dbUser = this.findUserByName(user.getName());
        //此用户不存在 或 密码错误
        if (null == dbUser || !user.getPassword().equals(dbUser.getPassword())) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //用户名 密码 匹配 签发token
        final UserDetails userDetails = this.loadUserByUsername(user.getName());

        final String token = jwtTokenUtil.generateToken(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }

        Map<String, Object> map = new HashMap<>(3);

        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", user.getName());
        map.put("roles", roles);

        //将token存入redis 过期时间 jwtConfig.time 单位[s]
        redisTemplate.opsForValue().
                set(JwtConfig.REDIS_TOKEN_KEY_PREFIX + user.getName(), jwtConfig.getPrefix() + token, jwtConfig.getTime(), TimeUnit.SECONDS);
        return map;

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
        userToAdd.setPassword(password);//加密密码
        userToAdd.setMail(mail);
        userToAdd.setName(name);
        userDao.saveUser(userToAdd);//保存角色

    }

    /**
     * 退出登录
     * 删除redis中的key
     */
    public void logout() {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        redisTemplate.delete(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);
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
     * 从token中提取信息
     */
    public UserDetails loadUserByToken(String authHeader) {
        final String authToken = authHeader.substring(jwtConfig.getPrefix().length());//除去前缀，获取token

        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        //token非法
        if (null == username) {
            return null;
        }
        String redisToken = redisTemplate.opsForValue().get(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);
        //从redis中取不到值或值不匹配
        if (!authHeader.equals(redisToken)) {
            return null;
        }
        List<String> roles = jwtTokenUtil.getRolesFromToken(authToken);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new org.springframework.security.core.userdetails.User(username, "***********", authorities);
    }
    /**
     * 根据用户名查询用户
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findUserByName(name);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        if(user.getIsAdmin() == 1)
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        else
            authorities.add(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
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
