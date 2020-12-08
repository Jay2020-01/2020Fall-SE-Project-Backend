package com.backend.server;

//<<<<<<< HEAD
import com.backend.server.dao.PaperDaoImp;
import com.backend.server.entity.Favor;
import com.backend.server.entity.Paper;
import com.backend.server.entity.User;
import com.backend.server.mapper.FavorMapper;
import com.backend.server.mapper.UserMapper;
import com.backend.server.service.FavorService;
import com.backend.server.service.FollowService;
import com.backend.server.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//=======
//>>>>>>> e282f4029d64703221443e67b4bf94e3d215cde0

@SpringBootTest(classes = BackendServerApplication.class)
class BackendServerApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FavorService favorService;
    @Autowired
    private FavorMapper favorMapper;
    @Autowired
    private FollowService followService;
    @Autowired
    private PaperDaoImp paperDaoImp;

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
    @Test
    void Demo(){
        List<Paper> papers = paperDaoImp.Demo("id","53e99e61b7602d97027281cd");
        System.out.println("papers = " + papers);
        System.out.println("===================");
        Paper paper = paperDaoImp.findPaperById("53e99e61b7602d97027281cd");
        System.out.println("paper = " + paper);
    }
    @Test
    void Demo2(){
        User user = userService.getUserById(5);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id",user.getId());
        List<Favor> ids = favorMapper.selectByMap(columnMap);
        List<Paper> paperList = new ArrayList<>();
        for(Favor f:ids) {
            paperList.add(paperDaoImp.findPaperById(f.getPaperId()));
        }
        System.out.println("paperList = " + paperList);
    }
}
