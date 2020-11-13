package com.backend.user.service;

import com.backend.common.entity.Paper;
import com.backend.common.entity.User;
import com.backend.user.dao.FavorDao;
import com.backend.user.dao.UserDao;
import com.backend.user.utils.DateUtil;
import com.backend.user.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavorService {
    @Autowired
    private UserDao userDao;
//    @Autowired
//    private PaperDao paperDao;
    @Autowired
    private FavorDao favorDao;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;

    // TODO
    public List<Paper> getFavorList(){

        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        List<Integer> ids = favorDao.getFavorList(user.getUserId());
        List<Paper> papers = new ArrayList<>();
        for(Integer i:ids) {
//            papers.add(paperDao.findPaperById(i));
        }
        return papers;
    }

    public void addFavor(Integer paperId, Integer userId){

        favorDao.addFavor(paperId,userId,dateUtil.getCurrentDate());
    }

    public void removeFavor(Integer paperId, Integer userId){

        favorDao.removeFavor(paperId,userId);
    }

    public boolean isFavored(Integer paperId, Integer userId) {
        return favorDao.isFavored(paperId, userId) != null;
    }
}
