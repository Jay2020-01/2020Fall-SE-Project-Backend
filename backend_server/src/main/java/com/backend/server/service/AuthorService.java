package com.backend.server.service;

import com.backend.server.entity.Author;
import com.backend.server.entity.pojo.Certification;
import com.backend.server.entity.pojo.Change;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
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
    
    public Page<Map> findAuthorByName(String name, Integer pageNum, Integer pageSize) {
    	Query query = new Query(Criteria.where("name").is(name));
    	Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Map> list = mongoTemplate.find(query.with(pageable), Map.class, "author");
        for (Map map : list) {
            String portalId = map.get("_id").toString();
            map.put("portalId", portalId);
        }
        long count = (pageNum + 1) * pageSize;
        if (list.size() == pageSize) count += pageSize;
        else if (list.size() == 0) count -= pageSize;
        return new PageImpl<Map>(list, pageable, count);
    }
}
