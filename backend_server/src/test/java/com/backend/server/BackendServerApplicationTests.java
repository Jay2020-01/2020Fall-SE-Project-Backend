package com.backend.server;

//<<<<<<< HEAD

import com.backend.server.dao.AuthorDaoImp;
import com.backend.server.dao.PaperDaoImp;
import com.backend.server.entity.Author;
import com.backend.server.entity.Follow;
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
    void demo(){
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("follower_id",2);
        List<Follow> ids = followMapper.selectByMap(columnMap);
        System.out.println("ids = " + ids);
        List<Author> authors = new ArrayList<>();
        for(Follow f:ids) {
            authors.add(authorDaoImp.findAuthorById(f.getFollowingId()));
        }
        System.out.println("authors = " + authors);
    }

    @Test
    void demo1(){
        List<Author> res = followService.getFollowList();
        for(Author author: res){
            if(author.getAid().equals("53f43d9cdabfaee43ec6115"))
                System.out.println("okk2");
        }
    }
    @Test
    void demo3(){
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("following_id","53f43d9cdabfaee43ec67555");
        int a = followMapper.selectByMap(columnMap).size();
        System.out.println("num3 = " + a);
    }
}
