package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Appeal {

    private Integer appealId;
    private String content;
    private Integer userId;

}
