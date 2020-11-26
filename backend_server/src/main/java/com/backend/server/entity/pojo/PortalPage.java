package com.backend.server.entity.pojo;

import com.backend.server.entity.Paper;
import lombok.Data;

import java.util.List;

@Data
public class PortalPage {
    private Integer id;
    private String expertName;
    private String expertPhoto;
    private String expertInfo;
    private Integer userId;
    private Integer hIndex;
    private Integer paperNum;
    private Integer quoteNum;
    private String position;
    private String affiliation;
    private String email;
    private String address;
    List<Paper> papers;
}
