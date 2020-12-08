package com.backend.server.service;

import com.backend.server.entity.Notice;
import com.backend.server.entity.pojo.Message;
import com.backend.server.mapper.NoticeMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public List<Notice> getMessageByUserId(Integer userId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("receiver_id", userId);
        columnMap.put("type", 1);
        List<Notice> notices = noticeMapper.selectByMap(columnMap);
        columnMap = new HashMap<>();
        columnMap.put("notifier_id", userId);
        columnMap.put("type", 1);
        notices.addAll(noticeMapper.selectByMap(columnMap));
        return notices;
    }


    public void sendMessage(Message message, Integer userId, String notifierName, String receiverName, Integer type) {
        Notice notice = new Notice();
        notice.setContent(message.getContent());
        notice.setNotifierId(userId);
        notice.setNotifierName(notifierName);
        notice.setReceiverId(message.getTarget_user_id());
        notice.setReceiverName(receiverName);
        notice.setCreateTime(new Timestamp(new Date().getTime()));
        notice.setType(type);
        notice.setIsRead(0);
        noticeMapper.insert(notice);
    }


    public List<Notice> getMessageByIds(Integer userId, Integer target) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>();
        queryWrapper.in("notifier_id", Arrays.asList(userId, target)).in("receiver_id",Arrays.asList(userId, target) ).orderByDesc("create_time");
        List<Notice> notices = noticeMapper.selectList(queryWrapper);
        return notices;
    }
}
