package com.mall.sso.service;



import com.github.pagehelper.Page;
import com.mall.api.CommonPage;
import com.mall.sso.dto.UmsMenuNode;
import com.mall.sso.model.UmsMenu;

import java.util.List;

/**
 * 后台菜单管理Service
 * Created by macro on 2020/2/2.
 */
public interface UmsMenuService {
    /**
     * 创建后台菜单
     */
    boolean create(UmsMenu umsMenu);

    /**
     * 修改后台菜单
     */
    boolean update(Long id, UmsMenu umsMenu);

    /**
     * 分页查询后台菜单
     */
    CommonPage list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<UmsMenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    boolean updateHidden(Long id, Integer hidden);

    UmsMenu getById(Long id);

    boolean removeById(Long id);
}
