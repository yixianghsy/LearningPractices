package com.mengxuegu.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.web.entities.SysPermission;
import com.mengxuegu.web.entities.SysRole;
import com.mengxuegu.web.mapper.SysPermissionMapper;
import com.mengxuegu.web.mapper.SysRoleMapper;
import com.mengxuegu.web.service.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {



    @Override
    public IPage<SysRole> selectPage(Page<SysRole> page, SysRole sysRole) {
        return baseMapper.selectPage(page, sysRole);
    }


    @Autowired
    private SysPermissionMapper sysPermissionMapper; // 报错正常， idea原因不识别

    @Override
    public SysRole findById(Long id) {
        if(id == null) {
            return new SysRole();
        }

        // 1. 通过角色id查询对应的角色信息
        SysRole sysRole = baseMapper.selectById(id);
        // 2. 通过角色id查询所拥有的权限
        List<SysPermission> permissions = sysPermissionMapper.findByRoleId(id);
        // 3. 将查询到的权限set到角色对象中SysRole
        sysRole.setPerList(permissions);
        return sysRole;
    }



    @Transactional
    @Override
    public boolean saveOrUpdate(SysRole entity) {
        entity.setUpdateDate(new Date());
        // 1. 更新角色表中的数据
        boolean flag = super.saveOrUpdate(entity);

        if(flag) {
            // 2. 更新角色权限关系表中的数据(删除)
            baseMapper.deleteRolePermissionByRoleId(entity.getId());

            // 2. 新增角色权限关系表中的数据
            if(CollectionUtils.isNotEmpty(entity.getPerIds())) {
                baseMapper.saveRolePermission(entity.getId(), entity.getPerIds());
            }
        }
        return flag;
    }


    @Transactional //事务管理
    @Override
    public boolean deleteById(Long id) {
        // 1. 通过id删除角色信息表数据
        baseMapper.deleteById(id);
        // 2. 通过id删除角色权限关系表数据
        baseMapper.deleteRolePermissionByRoleId(id);
        return true;
    }
}
