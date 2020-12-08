package com.backend.server;

import com.backend.server.dao.PaperDaoImp;
import com.backend.server.entity.User;
import com.backend.server.mapper.UserMapper;
import com.backend.server.service.FavorService;
import com.backend.server.service.FollowService;
import com.backend.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = BackendServerApplication.class)
class BackendServerApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FavorService favorService;
    @Autowired
    private FollowService followService;
    @Autowired
    private PaperDaoImp paperDaoImp;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        for(User u : users)
            System.out.println("u = " + u);
    }
}
