package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class User {

    private Integer userId;
    private String name;    //用户名
    private String password;// 性别
    private String gender;  // 邮箱
    private String mail;    // 手机号
    private String phone;   // 头像路径
    private String avatar;  // 专业领域
    private String major;   // 学校
    private String campus;  // 机构
    private String institution;//机构

}
