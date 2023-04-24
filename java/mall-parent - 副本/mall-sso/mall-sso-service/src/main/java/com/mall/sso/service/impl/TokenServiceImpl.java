package com.mall.sso.service.impl;


import com.mall.mapper.pojo.TbUser;
import com.mall.sso.service.TokenService;
import com.mall.utils.E3Result;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;


@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public E3Result getUserByToken(String token) {
        //根据token到redis中取用户信息
        String json=  stringRedisTemplate.opsForValue().get("SESSION:" + token);
        //取不到用户信息，登录已经过期，返回登录过期
        if (StringUtils.isBlank(json)){
            return E3Result.build(201,"用户登录已经过去");
        }
        //取到用户信息更新token的过期时间
        stringRedisTemplate.expire(token, SESSION_EXPIRE, TimeUnit.SECONDS);
        //返回结果，E3Result其中包含TbUser对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(user);
    }
}
