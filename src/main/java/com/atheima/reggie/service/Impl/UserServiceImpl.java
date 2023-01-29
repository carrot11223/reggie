package com.atheima.reggie.service.Impl;

import com.atheima.reggie.entity.User;
import com.atheima.reggie.mapper.UserMapper;
import com.atheima.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
