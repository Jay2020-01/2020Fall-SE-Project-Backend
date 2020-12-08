package com.backend.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "notice")
public class Notice {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer notifierId;
    private String notifierName;
    private Integer receiverId;
    private String receiverName;
    private String content;
    private Integer isRead;
    private Integer type;
    private Date createTime;
}
