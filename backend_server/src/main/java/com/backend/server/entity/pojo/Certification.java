package com.backend.server.entity.pojo;

import lombok.Data;

@Data
public class Certification {
    private Integer userId;
    private String portalId;
    private String email;
    private String code;
}
