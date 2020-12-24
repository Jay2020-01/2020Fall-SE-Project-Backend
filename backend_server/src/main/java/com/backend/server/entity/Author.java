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
    private String _id;

    private String aid;
    private String name;
    private Integer h_index;
    private List<tag> tags;
    private List<pub> pubs;
    private Integer n_pubs;
    private Integer n_citation;
    private List<String> orgs;
    private String position;
    private List<String> pidDoc;
    private Integer userId;
}
