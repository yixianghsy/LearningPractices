package com.mall.sso.controller;

import com.mall.sso.service.TokenService;
import com.mall.utils.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 根据token查询用户信息Controller
 * <p>Title: TokenController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class TokenController {
    @Reference
    private TokenService tokenService;


    @RequestMapping(value="/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        E3Result userByToken = tokenService.getUserByToken(token);
        //响应结果之前，判断是否为jsonp请求
        if(StringUtils.isNotBlank(callback)){
            //把结果封装成一个js语句响应
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userByToken);
//            mappingJacksonValue.setJsonpFunction(callback);
            return  mappingJacksonValue;
        }
        return  userByToken;
    }
}
