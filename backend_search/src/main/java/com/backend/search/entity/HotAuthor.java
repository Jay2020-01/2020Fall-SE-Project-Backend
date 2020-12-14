package com.backend.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Data
//@Document(collection = "c_h_author")
public class HotAuthor implements Serializable {

    @Id
    private String _id;

    private String aid;
    private String name;
    private Integer hIndex;
    private List<tag> tags;
    private List<pub> pubs;
    private Integer nPubs;
    private Integer nCitation;
    private List<String> orgs;
    private String position;
    private List<String> pidDoc;
    private Integer userId;
}
class tag {
    private Integer w;
    private String t;
}
class pub {
    private String i;
    private Integer r;
}

