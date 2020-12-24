package com.backend.server.service;

import com.backend.server.entity.Author;
import com.backend.server.entity.pojo.Certification;
import com.backend.server.entity.pojo.Change;
import com.backend.server.entity.pojo.MessageList;
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
     * @param aid
     * @return
     */
    public Map queryAuthor(String aid) {

        Query query = new Query(Criteria.where("aid").is(aid));
        Map authors = mongoTemplate.findOne(query, Map.class, "author");
        //System.out.println(authors);
        return authors;
    }

    /**
     * 绑定门户
     * @param userId
     * @param username
     * @param authorId
     */
    public void bindUser(Integer userId, String username, String authorId) {
        Query query = new Query(Criteria.where("aid").is(authorId));
        Update update = new Update().set("user_id", userId).set("username", username);
        mongoTemplate.updateFirst(query, update, "author");
    }


    /**
     * 更新门户信息
     * @param author
     */
    public void updateById(Change author) {
        Query query = new Query(Criteria.where("aid").is(author.getAid()));
        Update update = new Update().set("name", author.getExpertName()).set("phone", author.getPhoneNumber()).set("email", author.getEmail())
                                    .set("orgination", author.getOrgination()).set("address", author.getAddress()).set("homepage", author.getHomepage())
                                    .set("sex", author.getSex()).set("expertInfo", author.getExpertInfo()).set("work", author.getWork()).set("edu", author.getEdu());
        mongoTemplate.updateFirst(query, update,"author");
    }

    /**
     * 搜索门户
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
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

    /**
     * 解除门户绑定
     * @param aid
     */
    public void unBindById(String aid) {
        Query query = new Query(Criteria.where("aid").is(aid));
        Update update = new Update().unset("user_id");
        mongoTemplate.updateFirst(query, update, "author");
    }


    public void getNameByUserId(List<MessageList> personList) {
        for (MessageList messageList : personList) {
            Query query = new Query(Criteria.where("user_id").is(messageList.getId()));
            Map author = mongoTemplate.findOne(query, Map.class, "author");
            if(author != null) messageList.setAuthorName((String) author.get("name"));
        }
    }
}
