package com.mall.sso.service;


import com.mall.mapper.pojo.TbUser;
import com.mall.utils.E3Result;

public interface RegisterService {
    E3Result checkData(String param, int type);
    E3Result register(TbUser user);
}
