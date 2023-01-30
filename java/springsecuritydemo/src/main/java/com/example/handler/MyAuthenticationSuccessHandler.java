package com.example.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录成功类
 * 继承接口，用于处理成功的用户身份验证的策略
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    // 提供了读取和写入JSON的功能，可以与基本pojo类进行交互，也可以与通用JSON树模型进行交互，还提供了执行转换的相关功能。
    @Autowired
    private ObjectMapper objectMapper;

    // 当用户已成功通过身份验证时调用。
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");
        response.setContentType("application/json;charset=utf-8");
        // writeValueAsString：将java对象序列化为字符串
        response.getWriter().write(objectMapper.writeValueAsString(authentication));
    }
}

