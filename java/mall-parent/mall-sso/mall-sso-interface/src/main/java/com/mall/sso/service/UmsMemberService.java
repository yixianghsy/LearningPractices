package com.mall.sso.service;

import com.mall.api.CommonResult;
import com.mall.sso.model.UmsAdmin;
import com.mall.sso.model.UmsMember;

/**
 * 会员管理Service
 * Created on 2018/8/3.
 */
public interface UmsMemberService {
    /**
     * 根据用户名获取会员
     */
    UmsMember getByUsername(String username);

    /**
     * 根据会员编号获取会员
     */
    UmsMember getById(Long id);

    /**
     * 用户注册
     */

    UmsMember register(UmsMember umsMemberParam);

    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 修改密码
     */

    CommonResult updatePassword(String telephone, String password, String authCode);

    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();

    /**
     * 根据会员id修改会员积分
     */
    void updateIntegration(Long id,Integer integration);

    /**
     * 获取用户信息
     */
    UmsMember loadUserByUsername(String username);

    /**
     * 登录后获取token
     */
    UmsMember login(String username, String password);

    /**
     * 刷新token
     */
    String refreshToken(String token);

    int updateUmsMember(UmsMember umsMember);
}
