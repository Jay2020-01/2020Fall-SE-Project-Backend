package com.backend.server;

import com.backend.server.entity.User;
import com.backend.server.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = BackendServerApplication.class)
class BackendServerApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        for(User u : users)
            System.out.println("u = " + u);
    }
}
