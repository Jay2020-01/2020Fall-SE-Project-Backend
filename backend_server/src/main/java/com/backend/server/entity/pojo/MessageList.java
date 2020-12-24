package com.backend.server.entity.pojo;

import lombok.Data;

@Data
public class MessageList {
    private Integer id;
    private String userName; // 用户名
    private String avatar;   // 头像路径
    private Integer isAdmin; // 是否管理员
    private String authorName;
    private String aid;
}
