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

    List<User> findAll();

    User findByUserId(Integer userId);
}
