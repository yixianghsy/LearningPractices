package com.mall.order.interceptor;


import com.mall.cart.service.CartService;
import com.mall.mapper.pojo.TbItem;
import com.mall.mapper.pojo.TbUser;
import com.mall.sso.service.TokenService;
import com.mall.utils.CookieUtils;
import com.mall.utils.E3Result;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 * <p>Title: LoginInterceptor</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${SSO_URL}")
    private String SSO_URL;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //判断token是否存在
        if (StringUtils.isBlank(token)) {
            //如果token不存在，未登录状态，跳转到sso系统的登录页面。用户登录成功后，跳转到当前请求的url
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        //如果token存在，需要调用sso系统的服务，根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //如果取不到，用户登录已经过期，需要登录。
        if (e3Result.getStatus() != 200) {
            //如果token不存在，未登录状态，跳转到sso系统的登录页面。用户登录成功后，跳转到当前请求的url
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        //如果取到用户信息，是登录状态，需要把用户信息写入request。
        TbUser user = (TbUser) e3Result.getData();
        request.setAttribute("user", user);
        //判断cookie中是否有购物车数据，如果有就合并到服务端。
        String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNoneBlank(jsonCartList)) {
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
        }
        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
