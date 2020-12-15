package com.backend.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Data
//@Document(collection = "c_h_author")
public class HotAuthor {

    @Id
    private String _id;

    private String aid;
    private String name;
    private Integer hIndex;
    private Integer nPubs;
    private Integer nCitation;
    private Integer userId;
}

