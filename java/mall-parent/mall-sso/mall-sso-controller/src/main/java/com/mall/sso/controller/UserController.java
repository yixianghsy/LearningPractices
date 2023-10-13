package com.mall.sso.controller;


import com.mall.modules.user.TbUser;
import com.mall.sso.service.UserService;
import com.mall.utils.CookieUtils;
import com.mall.utils.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
        return userService.checkData(param, type);
    }

    @PostMapping("/register")
    @ResponseBody
    public E3Result register(TbUser tbUser) {
        return userService.register(tbUser);
    }

//    @PostMapping("/login")
//    @ResponseBody
//    public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
//        E3Result result = userService.login(username, password);
//        // Token写入cookie 浏览器关闭就过期
//        if (result.getStatus() == 200) {
//            String token = result.getData().toString();
//            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
//        }
//        return result;
//    }

    @RequestMapping("/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        System.out.println("token获取"+token);
        E3Result result = userService.getUserByToken(token);
        System.out.println("token获取"+result);
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
//            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

}
