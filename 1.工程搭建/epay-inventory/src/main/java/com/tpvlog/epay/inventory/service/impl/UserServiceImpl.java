package com.tpvlog.epay.inventory.service.impl;

import com.tpvlog.epay.inventory.entity.User;
import com.tpvlog.epay.inventory.mapper.UserMapper;
import com.tpvlog.epay.inventory.service.IRedisService;
import com.tpvlog.epay.inventory.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IRedisService redisService;

    @Override
    public User queryUserByName(String name) {
        redisService.set("ressmix","喜欢小玉");
        String v = redisService.get("ressmix");

        LOGGER.info("redis value:{}", v);

        return userMapper.queryUserByName(name);
    }
}
