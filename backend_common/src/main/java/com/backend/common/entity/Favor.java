package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class Favor {

    private Integer paperId;
    private Integer userId;
    private Date favorTime;

}
