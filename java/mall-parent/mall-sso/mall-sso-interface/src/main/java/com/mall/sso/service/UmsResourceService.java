package com.mall.sso.service;


import com.github.pagehelper.Page;
import com.mall.api.CommonPage;
import com.mall.sso.dto.ResourceRoleDTO;
import com.mall.sso.model.UmsResource;

import java.util.List;

/**
 * 后台资源管理Service
 * Created by macro on 2020/2/2.
 */
public interface UmsResourceService {
    /**
     * 添加资源
     */
    boolean create(UmsResource umsResource);

    /**
     * 修改资源
     */
    boolean update(Long id, UmsResource umsResource);

    /**
     * 删除资源
     */
    boolean delete(Long id);

    /**
     * 分页查询资源
     */
    CommonPage list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);
    List<UmsResource> list();
    List<ResourceRoleDTO> getAllResourceRole();

    UmsResource getById(Long id);
}
