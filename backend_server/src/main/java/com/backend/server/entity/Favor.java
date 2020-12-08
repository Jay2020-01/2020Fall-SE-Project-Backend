package com.backend.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName(value = "favor")
public class Favor {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String paperId;
    private Date favorTime;

}
