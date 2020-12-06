package com.backend.server.service;

import com.backend.server.entity.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Paper> queryPaperByAuthorId(String portalId) {
        Query query = new Query(Criteria.where("authors.id").is(portalId));
        List<Paper> papers = mongoTemplate.find(query, Paper.class);
        return papers;
    }
}
