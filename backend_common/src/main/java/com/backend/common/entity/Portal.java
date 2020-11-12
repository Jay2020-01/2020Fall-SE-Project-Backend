package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Portal {
    private Integer portalId;
    private String expertInfo;
    private Integer userId;

}
