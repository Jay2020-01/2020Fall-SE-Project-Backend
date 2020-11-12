package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class Follow {

    private Integer followerId;
    private Integer followingId;
    private Date followTime;

}
