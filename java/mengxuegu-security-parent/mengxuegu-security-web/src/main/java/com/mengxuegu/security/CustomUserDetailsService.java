package com.mengxuegu.security;

import com.mengxuegu.web.entities.SysUser;
import com.mengxuegu.web.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 查询数据库中的用户信息
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Component("customUserDetailsService")
//public class CustomUserDetailsService implements UserDetailsService {
public class CustomUserDetailsService extends AbstractUserDetailsService{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired // 不能删掉，不然报错
    PasswordEncoder passwordEncoder;

    @Autowired
    SysUserService sysUserService;


    @Override
    public SysUser findSysUser(String usernameOrMobile) {
        logger.info("请求认证的用户名: " + usernameOrMobile);
        // 1. 通过请求的用户名去数据库中查询用户信息
        return sysUserService.findByUsername(usernameOrMobile);
    }

}