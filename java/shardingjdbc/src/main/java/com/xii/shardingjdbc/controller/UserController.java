package com.xii.shardingjdbc.controller;

import com.xii.shardingjdbc.entity.User;
import com.xii.shardingjdbc.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @RequestMapping("addUser")
    public String insertUser(){
        User user = new User();
        user.setNickname("wxt"+ new Random().nextInt());
        user.setPassword(123456);
        user.setAge(19);
        user.setSex(0);
        userMapper.addUser(user);
        return "success";
    }

    @GetMapping("getUser")
    public List<User> getUser(){
        return userMapper.getUserList();
    }
}
