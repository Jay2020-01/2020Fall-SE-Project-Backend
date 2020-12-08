package com.backend.server.service;

import com.backend.server.entity.pojo.Certification;
import com.backend.server.entity.pojo.Change;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthorService {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 查看作者信息
     * @param portalId
     * @return
     */
    public Map queryAuthor(String portalId) {

        Query query = new Query(Criteria.where("_id").is(new ObjectId(portalId)));
        Map authors = mongoTemplate.findOne(query, Map.class, "author");
        //System.out.println(authors);
        return authors;
    }

    public void bindUser(Certification certification) {
        Integer userId = certification.getUserId();
        String authorId = certification.getPortalId();
        Query query = new Query(Criteria.where("_id").is(new ObjectId(authorId)));
        Update update = new Update().set("user_id", userId);
        mongoTemplate.updateFirst(query, update, "author");
    }


    public void updateById(Change author) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(author.get_id())));
        Update update = new Update().set("position", author.getPosition()).set("name", author.getName());
        mongoTemplate.updateFirst(query, update,"author");
    }
}
