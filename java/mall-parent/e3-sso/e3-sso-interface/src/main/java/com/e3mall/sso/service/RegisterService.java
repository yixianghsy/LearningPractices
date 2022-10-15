package com.e3mall.sso.service;


import com.e3mall.mapper.pojo.TbUser;
import com.e3mall.utils.E3Result;

public interface RegisterService {
    E3Result checkData(String param, int type);
    E3Result register(TbUser user);
}
