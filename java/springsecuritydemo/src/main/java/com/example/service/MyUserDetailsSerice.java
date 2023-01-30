package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义登录接口(核心接口，加载用户特定的数据。)
 */
@Component
public class MyUserDetailsSerice implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    /**
     * 日志 返回与作为参数传递的类对应的日志程序
     */
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
    /**
     * 校验，根据用户名定位用户
     * @param username 标识需要其数据的用户的用户名。
     * @return 核心用户信息，一个完全填充的用户记录
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = passwordEncoder.encode("123");
        logger.info("登录，用户名：{}，密码：{}", username,password);
        return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
