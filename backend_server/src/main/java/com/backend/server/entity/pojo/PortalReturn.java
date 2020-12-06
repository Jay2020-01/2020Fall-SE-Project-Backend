package com.backend.server.entity.pojo;

import com.backend.server.entity.Author;
import com.backend.server.entity.Paper;
import lombok.Data;

import java.util.List;

@Data
public class PortalReturn {
    List<Author> authors;
    List<Paper> papers;

    public PortalReturn(List<Author> authors, List<Paper> papers) {
        this.authors = authors;
        this.papers = papers;
    }
}
