package com.backend.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaperService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map> queryPaperByAuthorId(String portalId) {
        Query query = new Query(Criteria.where("authors.id").is(portalId));
        List<Map> papers = mongoTemplate.find(query, Map.class, "paper");
        return papers;
    }
}
