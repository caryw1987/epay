package com.tpvlog.epay.inventory.controller;

import com.tpvlog.epay.inventory.entity.User;
import com.tpvlog.epay.inventory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/getUserInfo/{name}")
    @ResponseBody
    public User getUserInfo(@PathVariable("name") String name) {
        User user = userService.queryUserByName(name);
        return user;
    }
}
