package com.backend.server.entity;

import com.backend.server.entity.authorSon.pub;
import com.backend.server.entity.authorSon.tag;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document(collection = "author")
public class Author implements Serializable {

    @Id
    private Long _id;

    private String id;
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
