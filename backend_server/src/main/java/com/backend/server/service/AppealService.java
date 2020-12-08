package com.backend.server.service;

import com.backend.server.entity.Appeal;
import com.backend.server.entity.pojo.PostAppeal;
import com.backend.server.mapper.AppealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppealService {
    @Autowired
    private AppealMapper appealMapper;

    public Integer getAppealById(Integer appeal_id) {
        Appeal appeal = appealMapper.selectById(appeal_id);
        return appeal.getUserId();
    }

    public void insertAppeal(PostAppeal appeal, Integer userId) {
        Appeal insert = new Appeal();
        BeanUtils.copyProperties(appeal, insert);
        insert.setUserId(userId);
        insert.setIsDeal(0);
        appealMapper.insert(insert);
    }

    public void updateIsDeal(Integer appeal_id) {
        Appeal appeal = appealMapper.selectById(appeal_id);
        appeal.setIsDeal(1);
        appealMapper.updateById(appeal);
    }
}
