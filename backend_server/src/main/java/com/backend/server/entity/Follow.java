package com.backend.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName(value = "follow")
public class Follow {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer followerId;
    private Integer followingId;
    private Date followTime;

}
