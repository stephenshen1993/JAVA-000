package com.stephenshen.raws.service.impl;

import com.stephenshen.raws.domain.User;
import com.stephenshen.raws.mapper.UserMapper;
import com.stephenshen.raws.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName : UserServiceimpl  //类名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-02 12:50  //时间
 */
@Service("userService")
public class UserServiceimpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public void add(User user) {
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setUpdateTime(user.getCreateTime());
        userMapper.insert(user);
    }

    @Transactional
    @Override
    public void removeByUserId(Long userId) {
        userMapper.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public void modifyByUserId(User user) {
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userMapper.updateByUserId(user);
    }

    @Transactional
    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Transactional
    @Override
    public User findByUserId(Long userId) {
        return userMapper.selectByUserId(userId);
    }

    /**
     * 读写服务，用以测试数据源切换
     * @param user
     */
    public void readAndWrite(User user){
        User userOld = userMapper.selectByUserId(user.getUserId());
        LOGGER.info("old user:" + userOld.toString());
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userMapper.updateByUserId(user);
        User userNew = userMapper.selectByUserId(user.getUserId());
        LOGGER.info("new user:" + userNew.toString());
    }
}
