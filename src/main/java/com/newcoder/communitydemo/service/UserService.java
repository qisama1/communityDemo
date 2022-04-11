package com.newcoder.communitydemo.service;

import com.newcoder.communitydemo.dao.UserMapper;
import com.newcoder.communitydemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }


}
