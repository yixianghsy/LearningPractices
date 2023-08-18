package com.mall.sso.service;


import com.mall.modules.user.TbUser;
import com.mall.utils.E3Result;

public interface UserService {
    E3Result checkData(String param, Integer type);
    E3Result register(TbUser tbUser);
    E3Result login(String username, String password);
    E3Result getUserByToken(String token);
}
