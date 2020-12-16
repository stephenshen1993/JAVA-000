package io.stephenshen1993.rpcfx.demo.provider;

import io.stephenshen1993.rpcfx.demo.api.User;
import io.stephenshen1993.rpcfx.demo.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
