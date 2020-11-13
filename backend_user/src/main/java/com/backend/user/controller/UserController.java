package com.backend.user.controller;

import com.backend.common.entity.Result;
import com.backend.common.entity.StatusCode;
import com.backend.common.entity.User;
import com.backend.user.dao.UserDao;
import com.backend.user.service.UserService;
import com.backend.user.utils.FormatUtil;
import com.backend.user.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private FormatUtil formatUtil;

    /**
     * 登录返回token
     */
    @PostMapping("/login")
    public Result login(User user) {
        if (!formatUtil.checkStringNull(user.getName(), user.getPassword())) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        try {
            Map<String, Object> map = userService.login(user);
            return Result.create(StatusCode.OK, "登录成功", map);
        } catch (UsernameNotFoundException ue) {
            return Result.create(StatusCode.LOGINERROR, "登录失败，用户名或密码错误");
        } catch (RuntimeException re) {
            return Result.create(StatusCode.LOGINERROR, re.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(String name,String password,String mail, String mailCode) {

        if (!formatUtil.checkStringNull(name,password,mail,mailCode)) {
            return Result.create(StatusCode.ERROR, "注册失败，字段不完整");
        }
        try {
            userService.register(name,password,mail,mailCode);
            return Result.create(StatusCode.OK, "注册成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "注册失败，" + e.getMessage());
        }
    }

    /**
     * 用户退出登录
     * 删除redis中的token
     */
    @GetMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.create(StatusCode.OK, "退出成功");
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/updateInfo")
    public Result updateUser(String gender, String phone,String major, String campus, String institution){
        try {
            userService.updateUserInfo(gender,phone,major,campus,institution);
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
            User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            String path="/home/zero/avatar/";
            String format = formatUtil.getFileFormat(file.getOriginalFilename());
            File file1 = new File(path + user.getName() + format);
//            System.out.println(path + user.getUserId() + format);
            if(!file1.getParentFile().exists()){
                file1.getParentFile().mkdirs();
            }
            file.transferTo(file1);
            userDao.updateAvatar(user.getUserId(),"http://127.0.0.1/home/zero/avatar/"+user.getName()+format);
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
        String redisMailCode = userService.getMailCodeFromRedis(mail);

        if (redisMailCode != null) {
            return Result.create(StatusCode.ERROR, "1分钟内不可重发验证码");
        } else {
            userService.sendMail(mail);
            return Result.create(StatusCode.OK, "发送成功");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(String oldPassword, String newPassword) {
        try {
            userService.updateUserPassword(oldPassword, newPassword);
            return Result.create(StatusCode.OK, "修改密码成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    /*=============================================================*/

    @GetMapping("/getUser")
    public Result getUserInfo(){
        User user = userService.getUserInfo();
        return Result.create(StatusCode.OK, "获取成功",user);
    }
    @GetMapping("/getUser/{userName}")
    public Result getUserInfoByName(@PathVariable String userName){
        User user = userService.getUserInfoByName(userName);
        return Result.create(StatusCode.OK, "获取成功",user);
    }
    @RequestMapping("/getUserId")
    public Result getUserIdByName(String userName){
        Integer uid = userService.findUserByName(userName).getUserId();
        return Result.create(StatusCode.OK,"成功",uid);
    }

}
