package com.backend.server.entity.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PortalReturn {
    Map author;
    List<Map> papers;

    public PortalReturn(Map author, List<Map> papers) {
        this.author = author;
        this.papers = papers;
    }
}
