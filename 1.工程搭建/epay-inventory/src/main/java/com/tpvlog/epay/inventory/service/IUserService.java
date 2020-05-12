package com.tpvlog.epay.inventory.service;

import com.tpvlog.epay.inventory.entity.User;

public interface IUserService {
    User queryUserByName( String name);
}
