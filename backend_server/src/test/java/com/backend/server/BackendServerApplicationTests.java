package com.backend.server;

import com.backend.server.mapper.UserMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest(classes = BackendServerApplication.class)
class BackendServerApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        Integer userId = 1;
        String authorId = "5fc64fa39b5f861f21979158";
        Query query = new Query(Criteria.where("_id").is(new ObjectId(authorId)));
        Update update = new Update().set("user_id", userId);
        mongoTemplate.updateFirst(query, update, "author");
    }
}
