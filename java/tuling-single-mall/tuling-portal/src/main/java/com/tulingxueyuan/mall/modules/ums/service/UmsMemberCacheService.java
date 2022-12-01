package com.tulingxueyuan.mall.modules.ums.service;




import com.tulingxueyuan.mall.modules.ums.model.UmsMember;

/**
 * 后台用户缓存管理Service
 * Created by macro on 2020/3/13.
 */
public interface UmsMemberCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);


    /**
     * 获取缓存后台用户信息
     */
    UmsMember getUser(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setUser(UmsMember admin);
}
