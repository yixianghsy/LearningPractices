package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    /**
     * 测试登陆
     * @return
     */
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello controller";
    }

    /**
     * 登录页，跳转到/templates/login.html页面
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 首页，跳转到/templates/index.html页面
     * @return
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    // 当前用户信息
    @GetMapping("/info")
    @ResponseBody
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }


}
