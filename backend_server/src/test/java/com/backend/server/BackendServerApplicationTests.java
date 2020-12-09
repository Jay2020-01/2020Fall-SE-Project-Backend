package com.backend.server;

//<<<<<<< HEAD

import com.backend.server.dao.AuthorDaoImp;
import com.backend.server.dao.PaperDaoImp;
import com.backend.server.entity.*;
import com.backend.server.mapper.FavorMapper;
import com.backend.server.mapper.FollowMapper;
import com.backend.server.mapper.UserMapper;
import com.backend.server.service.FavorService;
import com.backend.server.service.FollowService;
import com.backend.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//=======
//>>>>>>> e282f4029d64703221443e67b4bf94e3d215cde0

@SpringBootTest(classes = BackendServerApplication.class)
class BackendServerApplicationTests {
    @Autowired
    private FavorMapper favorMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FavorService favorService;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private FollowService followService;
    @Autowired
    private PaperDaoImp paperDaoImp;
    @Autowired
    private AuthorDaoImp authorDaoImp;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void paper(){
//        List<Paper> papers = paperDaoImp.Demo("pid","53e99e61b7602d97027281cd");
//        System.out.println("papers = " + papers);
//        System.out.println("===================");
        Paper paper = paperDaoImp.findPaperById("53e99e61b7602d97027281ce");
        System.out.println("paper = " + paper);
//        System.out.println("====================");
//        Query query = new Query(Criteria.where("_id").is(new ObjectId("5fcee0a55e9f4bd378df2a3d")));
//        Paper paper1 = mongoTemplate.findOne(query, Paper.class, "paper");
//        System.out.println("paper1 = " + paper1);
    }
    @Test
    void favor(){
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id",5);
        List<Favor> ids = favorMapper.selectByMap(columnMap);
        List<Paper> paperList = new ArrayList<>();
        for(Favor f:ids) {
            paperList.add(paperDaoImp.Demo("id",f.getPaperId()).get(0));
        }
        System.out.println("paperList = " + paperList);
    }
    @Test
    void follow(){
        User user = userService.getUserById(1);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("follower_id",user.getId());
        List<Follow> ids = followMapper.selectByMap(columnMap);
        System.out.println("ids = " + ids);
        List<Author> authors = new ArrayList<>();
        for(Follow f:ids) {
            authors.add(authorDaoImp.findAuthorById(f.getFollowingId()));
        }
        System.out.println("authors = " + authors);
    }
    @Test
    void user(){
        User user = userMapper.selectById(1);
        System.out.println("user = " + user);
    }
}
