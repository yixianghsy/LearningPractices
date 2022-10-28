package com.xdclass.userapi.service;

import com.xdclass.userapi.VO.UserVO;

/**
 * @author daniel
 */
public interface IUserService {


    /**
     * 通过用户id获取用户信息接口
     * @param id
     * @return
     */
    public UserVO getUserById(int id);
}
