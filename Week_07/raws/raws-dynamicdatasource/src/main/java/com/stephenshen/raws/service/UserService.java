package com.stephenshen.raws.service;

import com.stephenshen.raws.domain.User;

import java.util.List;

/**
 * @ClassName : UserService  //类名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-02 12:49  //时间
 */
public interface UserService {

    /**
     * 插入一条记录
     * @param user
     */
    void add(User user);

    /**
     * 根据用户ID删除指定的一条数据
     * @param userId
     */
    void removeByUserId(Long userId);

    /**
     * 根据用户ID修改指定的一条记录
     * @param user
     */
    void modifyByUserId(User user);

    /**
     * 查找全部数据
     * @return
     */
    List<User> findAll();

    /**
     * 根据用户ID查找指定的一条数据
     * @param userId
     * @return
     */
    User findByUserId(Long userId);
}
