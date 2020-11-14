package com.backend.portal.entity;

import lombok.Data;

@Data
public class Certification {
    private Integer portalId;
    private Integer userId;
    private String expertName;
    private String email;
}
