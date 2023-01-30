package com.mengxuegu.security;

import com.mengxuegu.web.entities.SysUser;
import com.mengxuegu.web.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过手机号获取用户信息和权限资源
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Component("mobileUserDetailsService") // 一定不要少了
public class MobileUserDetailsService extends AbstractUserDetailsService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SysUserService sysUserService;

    @Override
    public SysUser findSysUser(String usernameOrMobile) {
        logger.info("请求的手机号是：" + usernameOrMobile);
        // 1. 通过手机号查询用户信息
        return sysUserService.findByMobile(usernameOrMobile);
    }

}