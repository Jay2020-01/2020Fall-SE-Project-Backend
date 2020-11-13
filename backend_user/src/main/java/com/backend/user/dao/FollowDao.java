package com.backend.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface FollowDao {

    /**
     *  关注列表 id
     */
    List<Integer> getFollowList(Integer id);

    /**
     * 查询是否已关注
     */
    Date findConnection(Integer followerId, Integer followingId);

    /**
     *  新增关注
     */
    void addConnection(Integer followerId,Integer followingId, Date time);

    /**
     * 移除关注
     */
    void removeConnection(Integer followerId,Integer followingId);

    /**
     * 关注数量
     */
    Long getFollowingCountById(Integer followerId);

    /**
     * 粉丝数量
     */
    Long getFollowerCountById(Integer followingId);
}
