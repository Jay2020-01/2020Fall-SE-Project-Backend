package com.backend.server;

//<<<<<<< HEAD

import com.backend.server.dao.AuthorDaoImp;
import com.backend.server.dao.PaperDaoImp;
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

    }
}
