package com.backend.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String userName;// 用户名
    private String password;// 密码
    private String gender;  // 性别
    private String avatar;  // 头像路径
    private String mail;    // 邮箱
    private String phone;   // 手机号
    private String major;   // 专业
    private String campus;  // 大学
    private String institution;//机构
    private Integer isAdmin;// 是否管理员

}
