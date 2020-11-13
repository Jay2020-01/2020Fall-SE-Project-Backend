package com.backend.portal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "portal")
public class Portal {
    private Integer portalId;
    private String expertInfo;
    private Integer userId;
}
