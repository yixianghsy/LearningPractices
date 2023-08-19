package com.mall.cart.interceptor;


import com.mall.modules.user.TbUser;
import com.mall.sso.service.UserService;
import com.mall.utils.CookieUtils;
import com.mall.utils.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录处理拦截器
 * <p>Title: LoginInterceptor</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Reference
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1、从Cookie中取出token
        String e3_token = CookieUtils.getCookieValue(request, "token");
        // 2、没有token，直接放行
        if (StringUtils.isBlank(e3_token)) {
            return true;
        }
        // 3、取到token，调用sso服务取出user信息
        E3Result e3Result = userService.getUserByToken(e3_token);
        // 4、没有用户信息直接放行
        if (e3Result.getStatus() != 200) {
            return true;
        }
        // 5、存在用户信息，则保存至request中
        request.setAttribute("user", (TbUser) e3Result.getData());
        return true;
    }
}
