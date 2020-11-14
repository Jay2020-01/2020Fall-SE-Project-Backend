package com.backend.portal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "portal")
public class Portal {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String expertInfo;
    private Integer userId;
    private String expertName;
    private String affiliation;
    private String email;
    private String expertPhoto;
}
