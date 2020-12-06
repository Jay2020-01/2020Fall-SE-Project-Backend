package com.backend.server.entity;

import com.backend.server.entity.paperSon.author;
import com.backend.server.entity.paperSon.venue;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "paper")
public class Paper {

    @Id
    private Integer id;
    private String title;
    private List<author> authors;
    private venue venue;
    private Integer year;
    private List<String> keywords;
    private Integer nCitation;
    private String pageStart;
    private String pageEnd;
    private String lang;
    private String volume;
    private String issue;
    private String issn;
    private String doi;
    private List<String> url;
    private String abstracts;
}
