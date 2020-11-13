package com.backend.user.mq;


import com.backend.user.utils.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 发送邮件的队列消费者
 */
@Component
@RabbitListener(queues = "MAIL")
public class MailListener {

    @Value("${spring.mail.username}")
    private String fromMail;

    private final Logger logger = LoggerUtil.loggerFactory(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @RabbitHandler
    public void executeSms(Map<String, String> map) {
        String mail = map.get("mail");
        String code = map.get("code");
        try {
            this.sendMail(mail, code);
            logger.info(mail + "-" + code + "-发送成功");
        } catch (Exception e) {
            logger.error(mail + code + "发送失败-" + e.getMessage());
        }
    }

    private void sendMail(String mail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(mail);
        message.setSubject("邮箱验证码");
        message.setText("邮箱验证码：" + code + "，1分钟内有效");
        mailSender.send(message);
    }
}
