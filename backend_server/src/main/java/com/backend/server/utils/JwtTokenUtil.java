package com.backend.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class JwtTokenUtil implements Serializable {

    private final String header = "Authorization";
    private final String secret = "defaultSecret";
    private final String md5Key = "randomKey";

    /**
     * 从request中获取用户名
     */
    public String getUsernameFromRequest(HttpServletRequest request) {
        String token = request.getHeader(header);
        return getUsernameFromToken(token);
    }
    /**
     * 从request中获取用户id
     */
    public Integer getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader(header);
        return Integer.valueOf(getUserIdFromToken(token));
    }
    /**
     * 获取用户名从token中
     */
    public String getUsernameFromToken(String token) {
        String userName = "";
        String userInfoStr = getUserInfoStrFromToken(token);
        if (isNotBlank(userInfoStr)) {
            String[] userInfoArr = userInfoStr.split(";;");
            if (userInfoArr.length > 1) {
                userName = userInfoArr[1];
            }
        }
        return userName;
    }

    /**
     * 获取用户id从token中
     */
    public String getUserIdFromToken(String token) {
        String userId = "";
        String userInfoStr = getUserInfoStrFromToken(token);
        if (isNotBlank(userInfoStr)) {
            String[] userInfoArr = userInfoStr.split(";;");
            if (userInfoArr.length > 0) {
                userId = userInfoArr[0];
            }
        }
        return userId;
    }

    /**
     * 获取用户登录类型从token中
     */
    public String getLoginTypeFromToken(String token) {
        String loginType = "";
        String userInfoStr = getUserInfoStrFromToken(token);
        if (isNotBlank(userInfoStr)) {
            String[] userInfoArr = userInfoStr.split(";;");
            if (userInfoArr.length > 2) {
                loginType = userInfoArr[2];
            }
        }
        return loginType;
    }

    /**
     * 获取用户信息从token中
     */
    public String getUserInfoStrFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt接收者
     */
    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取md5 key从token中
     */
    public String getMd5KeyFromToken(String token) {
        return getPrivateClaimFromToken(token, md5Key);
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 解析token是否正确,不正确会报异常<br>
     */
    public void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     *  验证token是否失效
     *  true:过期   false:没过期
     */
    public Boolean isTokenExpired(String token) {
        boolean flag=true;
        try {
            final Date expiration = getExpirationDateFromToken(token);
            flag = expiration.before(new Date());
        } catch (Exception ignored) {
        }
        return flag;
    }

    /**
     * 生成token(通过用户信息和签名时候用的随机数)
     */
    public String generateToken(String userInfoStr, String randomKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(md5Key, randomKey);
        return doGenerateToken(claims, userInfoStr);
    }

    /**
     * 生成token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        Long expiration = 604800L;
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 获取混淆MD5签名用的随机字符串
     */
    public String getRandomKey() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 判断字符串不为空
     */
    public static boolean isNotBlank(final String string) {
        return string != null && string.trim().length() > 0;
    }
}
