package com.backend.server.entity.pojo;

import lombok.Data;

@Data
public class Message {
    private String title;
    private String content;
    private Integer target_user_id;
}
