package com.backend.server.service;

import com.backend.server.entity.Author;
import com.backend.server.entity.pojo.Certification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 查看作者信息
     * @param portalId
     * @return
     */
    public List<Author> queryAuthor(String portalId) {
        Query query = new Query(Criteria.where("id").is(portalId));
        List<Author> authors = mongoTemplate.find(query, Author.class);
        return authors;
    }

    public void bindUser(Certification certification) {
        Integer userId = certification.getUserId();
        String authorId = certification.getPortalId();
        Query query = new Query(Criteria.where("id").is(authorId));
        Update update = new Update().set("user_id", userId);
        mongoTemplate.updateFirst(query, update, Author.class);
    }


    public void updateById(Author author) {
        Query query = new Query(Criteria.where("id").is(author.getId()));
        Update update = new Update().set("name", author.getName()).addToSet("position", author.getPosition());
        mongoTemplate.updateFirst(query, update, Author.class);
    }
}
