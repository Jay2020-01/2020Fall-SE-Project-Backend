package com.backend.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface FavorDao {

    /**
     * 查看收藏
     */
    List<Integer> getFavorList(Integer id);

    /**
     * 添加收藏
     */
    void addFavor(Integer paperId, Integer userId, Date time);

    /**
     * 取消收藏
     */
    void removeFavor(Integer paperId, Integer userId);

    /**
     * 是否已收藏
     */
    Integer isFavored(Integer paperId, Integer userId);
}
