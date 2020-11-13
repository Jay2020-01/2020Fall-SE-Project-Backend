package com.backend.user.dao;

import com.backend.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

    /**
     * 根据用户名查询用户
     */
    User findUserByName(String name);

    /**
     * 保存用户
     */
    void saveUser(User user);

    /**
     * 根据邮箱查询用户
     */
    User findUserByMail(String mail);

    /**
     * 根据id查询用户
     */
    User findUserById(Integer id);

    /**
     * 根据id 修改用户信息
     */
    void updateUser(User user);

    /**
     * 根据id 保存头像地址
     */
    void updateAvatar(Integer userId, String avatar);

}
