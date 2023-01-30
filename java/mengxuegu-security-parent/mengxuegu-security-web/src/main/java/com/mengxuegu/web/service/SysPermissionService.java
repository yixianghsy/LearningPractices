package com.mengxuegu.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.web.entities.SysPermission;

import java.util.List;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 通过用户id查询所拥有权限
     * @param userId
     * @return
     */
    List<SysPermission> findByUserId(Long userId);


    /**
     * 通过权限id删除权限资源
     * @param id
     * @return
     */
    boolean deleteById(Long id);

}
