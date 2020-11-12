package com.backend.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class Paper {

    private Integer paperId;
    private String title;
    private Date publishTime;
    private String keywords;
    private String content;
    private String abstracts;
    private String sourceLink;
    private String authorName;
    private Integer portalId;
    private Integer citation;

}
