package com.backend.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "paper")
public class Paper {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private Date publishTime;
    private String keywords;
    private String abstracts;
    private String content;
    private String sourceLink;
    private String authorName;
    private Integer portalId;
    private Integer citation;

}
