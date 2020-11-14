package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class User {

    private Integer userId;
    private String name;    // 用户名
    private String password;// 密码
    private String gender;  // 性别
    private String mail;    // 邮箱
    private String phone;   // 手机号
    private String avatar;  // 头像路径
    private String major;   // 专业
    private String campus;  // 大学
    private String institution;//机构
    private Integer isAdmin;// 是否管理员

}