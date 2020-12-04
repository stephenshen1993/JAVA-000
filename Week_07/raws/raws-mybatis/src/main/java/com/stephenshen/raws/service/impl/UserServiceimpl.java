package com.stephenshen.raws.service.impl;

import com.stephenshen.raws.domain.User;
import com.stephenshen.raws.mapper.UserMapper;
import com.stephenshen.raws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName : UserServiceimpl  //类名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-02 12:50  //时间
 */
@Service("userService")
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public User findByUserId(Integer userId) {
        User user = userMapper.selectByUserId(userId);
        return user;
    }
}
