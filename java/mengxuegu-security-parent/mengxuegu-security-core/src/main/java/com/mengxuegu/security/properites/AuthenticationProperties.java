package com.mengxuegu.security.properites;

import lombok.Data;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Data
public class AuthenticationProperties {

    private String loginPage = "/login/page";
    private String loginProcessingUrl = "/login/form";
    private String usernameParameter = "name";
    private String passwordParameter = "pwd";
    private String[] staticPaths = {"/dist/**", "/modules/**", "/plugins/**"};

    /**
     * 认证响应的类型： JSON 、 REDIRECT 重定向
     */
    private LoginResponseType loginType = LoginResponseType.REDIRECT;

    /**
     *  获取图形验证码地址
     */
    private String imageCodeUrl = "/code/image";
    /**
     * # 发送手机验证码地址
     */
    private String mobileCodeUrl = "/code/mobile";
    /**
     * # 前往手机登录页面
     */
    private String mobilePage = "/mobile/page";
    /**
     * # 记住我功能有效时长
     */
    private Integer tokenValiditySeconds = 60*60*24*7;

}
