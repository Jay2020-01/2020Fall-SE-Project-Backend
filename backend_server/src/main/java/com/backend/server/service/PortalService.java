package com.backend.server.service;

import com.backend.server.entity.Portal;
import com.backend.server.entity.pojo.Certification;
import com.backend.server.mapper.PortalMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PortalService {
    @Autowired
    private PortalMapper portalMapper;
    @Autowired
    private static RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public Portal selectById(Integer portalId) {
        Portal portal = portalMapper.selectById(portalId);
        return portal;
    }

    public int updateById(Portal portal) {
        int result = portalMapper.updateById(portal);
        return result;
    }

    /*
    public boolean checkInformation(Certification certification) {
        Portal portal = portalMapper.selectById(certification.getPortalId());
        if (portal.getExpertName().equals(certification.getExpertName())) {
            return true;
        }
        return false;
    }*/

    public void relateUserToPortal(Certification certification, Integer userId) {
        Portal portal = portalMapper.selectById(certification.getPortalId());
        portal.setUserId(userId);
        portalMapper.updateById(portal);
    }

    /**
     * 判断验证码
     * @param mail 邮箱
     * @param code 验证码
     * @return 验证码正误
     */
    public boolean checkMailCode(String mail, String code) {
        String mailCode = redisTemplate.opsForValue().get("MAIL_" + mail);
        return code.equals(mailCode);
    }

    /**
     * 发送邮件验证码
     * @param mail 邮箱
     */
    public void sendMail(String mail) {
        Random _random = new Random();
        int random = _random.nextInt(899999) + 100001;
        Map<String, String> map = new HashMap<>();
        String code = Integer.toString(random);
        map.put("mail", mail);
        map.put("code", code);
        //保存发送记录
        redisTemplate.opsForValue()
                .set("MAIL_" + mail, code, 1, TimeUnit.MINUTES);
        rabbitTemplate.convertAndSend("MAIL", map);
    }
}
