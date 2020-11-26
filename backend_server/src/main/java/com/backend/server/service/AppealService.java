package com.backend.server.service;

import com.backend.server.entity.Appeal;
import com.backend.server.mapper.AppealMapper;
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
}
