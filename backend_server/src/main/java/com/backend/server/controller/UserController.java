package com.backend.server.controller;

import com.backend.server.entity.pojo.Result;
import com.backend.server.entity.pojo.StatusCode;
import com.backend.server.entity.User;
import com.backend.server.utils.JwtTokenUtil;
import com.backend.server.mapper.UserMapper;
import com.backend.server.service.PortalService;
import com.backend.server.service.UserService;
import com.backend.server.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private PortalService portalService;
    @Autowired
    private FormatUtil formatUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/demo")
    public Result demo() {
        return Result.create(StatusCode.OK,"ok","test success");
    }
    @RequestMapping("/demo/{id}")
    public Result demo(@PathVariable Integer id) {
        String name = jwtTokenUtil.getUsernameFromRequest(request);
        Integer userId = jwtTokenUtil.getUserIdFromRequest(request);
        return Result.create(StatusCode.OK,"ok",name+" : "+userId);
    }

    /**
     * 登录返回token
     */
    @PostMapping("/login")
    public Result login(String username, String password) {
        if (!formatUtil.checkStringNull(username, password)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        try {
            Map<String, Object> map = userService.login(username, password);
            if(map==null) return Result.create(StatusCode.LOGIN_ERROR, "登录失败，用户名或密码错误");
            return Result.create(StatusCode.OK, "登录成功", map);
        } catch (RuntimeException ue) {
            return Result.create(StatusCode.LOGIN_ERROR, "登录失败，用户名或密码错误");
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(String name,String password,String mail) {
        if (!formatUtil.checkStringNull(name,password,mail)) {
            return Result.create(StatusCode.ERROR, "注册失败，字段不完整");
        }
        try {
            Map<String, Object> map = userService.register(name,mail,password);
            return Result.create(StatusCode.OK, "注册成功",map);
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "注册失败，" + e.getMessage());
        }
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/change_info")
    public Result updateUser(String nickname,String family_name,String name,String gender, String occupation, String institution){
        try {
            userService.updateInfo(nickname,family_name,name,gender,occupation,institution);
            return Result.create(StatusCode.OK, "更新成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "更新失败，" + e.getMessage());
        }
    }

    /**
     * 上传头像
     */
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
            String path="/home/zero/avatar/";
            String format = formatUtil.getFileFormat(file.getOriginalFilename());
            File file1 = new File(path + user.getUserName() + format);
//            System.out.println(path + user.getId() + format);
            if(!file1.getParentFile().exists()){
                file1.getParentFile().mkdirs();
            }
            file.transferTo(file1);
            userService.updateAvatar("http://127.0.0.1/home/zero/avatar/"+user.getUserName()+format);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Result.create(200, "上传头像成功");
    }

    /**
     * 发送验证邮件
     * 异步发送
     */
    @PostMapping("/sendMail")
    public Result sendMail(String mail) {

        if (!(formatUtil.checkStringNull(mail)) || (!formatUtil.checkMail(mail))) {
            return Result.create(StatusCode.ERROR, "邮箱格式错误");
        }
        String redisMailCode = redisTemplate.opsForValue().get("MAIL_" + mail);

        if (redisMailCode != null) {
            return Result.create(StatusCode.ERROR, "1分钟内不可重发验证码");
        } else {
            portalService.sendMail(mail);
            return Result.create(StatusCode.OK, "发送成功");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/change_password")
    public Result updatePassword(String newPassword) {
        try {
            userService.updatePassword(newPassword);
            return Result.create(StatusCode.OK, "修改密码成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    /**
     * Token校检
     */
    @ResponseBody
    @RequestMapping(value = { "/checkToken" }, method = RequestMethod.POST)
    public int checkToken(@RequestParam(value = "authToken") String authToken) {
        if (JwtTokenUtil.isNotBlank(authToken)) {
            try {
                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
                if (flag) return 201;// 过期
                else return 200; // 认证通过
            } catch (Exception e) {
                e.printStackTrace();
            }
        } return 202; //为空
    }

    /*=============================================================*/

    @GetMapping("/my_info")
    public Result getCurUser(){
        User user = userMapper.selectById(jwtTokenUtil.getUserIdFromRequest(request));
        return Result.create(StatusCode.OK, "获取成功",user);
    }
    @GetMapping("/getUser/{userId}")
    public Result getUserByName(@PathVariable Integer userId){
        User user = userService.getUserById(userId);
        return Result.create(StatusCode.OK, "获取成功",user);
    }
    @RequestMapping("/getId")
    public Result getUserIdByName(String userName){
        Integer uid = userService.getUserByName(userName).getId();
        return Result.create(StatusCode.OK,"成功",uid);
    }



}
