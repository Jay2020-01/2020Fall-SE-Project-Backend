package com.backend.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "appeal")
public class Appeal {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer userId;

}
