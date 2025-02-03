package com.mall.portal.service;

import com.mall.sso.model.UmsMember;

public interface UmsMemberService {
    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();
}
