package com.backend.user.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtConfig {
    public static final String REDIS_TOKEN_KEY_PREFIX = "TOKEN_";
    private long time = 432000;     // 5天(以秒s计)过期时间
    private String secret = "Secret";// JWT密码
    private String prefix = "Bearer ";   // Token前缀
    private String header = "Authorization"; // 存放Token的Header Key

}
