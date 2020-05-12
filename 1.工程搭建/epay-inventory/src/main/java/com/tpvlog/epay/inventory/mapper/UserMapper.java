package com.tpvlog.epay.inventory.mapper;

import com.tpvlog.epay.inventory.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User queryUserByName(@Param("name") String name);
}
