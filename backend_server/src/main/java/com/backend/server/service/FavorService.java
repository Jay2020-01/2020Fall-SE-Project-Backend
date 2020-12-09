package com.backend.server.service;

import com.backend.server.dao.PaperDaoImp;
import com.backend.server.entity.Favor;
import com.backend.server.entity.Paper;
import com.backend.server.entity.User;
import com.backend.server.utils.JwtTokenUtil;
import com.backend.server.mapper.FavorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class FavorService {
    @Autowired
    private FavorMapper favorMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PaperDaoImp paperDaoImp;

    /**
     * 获取当前用户收藏列表
     */
    public List<Paper> getFavorList(){
        User user = userService.getUserById(jwtTokenUtil.getUserIdFromRequest(request));
//        User user = userService.getUserById(1);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id",user.getId());
        List<Favor> ids = favorMapper.selectByMap(columnMap);
        List<Paper> paperList = new ArrayList<>();
        for(Favor f:ids) {
//            paperList.add(paperDaoImp.Demo("id",f.getPaperId()).get(0));
            paperList.add(paperDaoImp.findPaperById(f.getPaperId()));
        }
        return paperList;
    }

    /**
     * 添加收藏
     * @param paperId 论文id
     * @param userId 用户id
     */
    public void addFavor(String paperId, Integer userId){
        Favor favor = new Favor(null, userId, paperId, new Date());
        favorMapper.insert(favor);
    }

    /**
     * 移除收藏
     * @param paperId 论文id
     * @param userId 用户id
     */
    public void removeFavor(String paperId, Integer userId){
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("paper_id",paperId);
        columnMap.put("user_id", userId);
        favorMapper.deleteByMap(columnMap);
    }

    /**
     * 是否已收藏
     * @param paperId 论文id
     * @param userId 用户id
     */
    public boolean isFavored(String paperId, Integer userId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("paper_id",paperId);
        columnMap.put("user_id", userId);
        List<Favor> favorList = favorMapper.selectByMap(columnMap);
        return favorList.size() == 1;
    }
}
