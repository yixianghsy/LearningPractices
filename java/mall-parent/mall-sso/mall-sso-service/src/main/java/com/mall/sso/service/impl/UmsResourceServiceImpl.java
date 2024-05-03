package com.mall.sso.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.mall.api.CommonPage;
import com.mall.sso.dto.ResourceRoleDTO;
import com.mall.sso.mapper.UmsAdminMapper;
import com.mall.sso.mapper.UmsResourceMapper;
import com.mall.sso.model.UmsResource;
import com.mall.sso.model.UmsResourceExample;
import com.mall.sso.service.UmsAdminCacheService;
import com.mall.sso.service.UmsResourceService;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
;

import java.util.Date;
import java.util.List;

/**
 * 后台资源管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    private UmsAdminCacheService adminCacheService;
    @Autowired
    private UmsResourceMapper resourceMapper;
    @Autowired
    private  UmsResourceMapper umsResourceMapper;
    @Override
    public boolean create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        int count = resourceMapper.insert(umsResource);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }

    @Override
    public boolean update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        int count = resourceMapper.updateByPrimaryKeySelective(umsResource);
        adminCacheService.delResourceListByResource(id);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }
    @Override
    public boolean delete(Long id) {
        int count = resourceMapper.deleteByPrimaryKey(id);
        adminCacheService.delResourceListByResource(id);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }
    // TODO 返回值需要修改
    @Override
    public CommonPage list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        //因为加这个就报错，先注释掉
        PageHelper.startPage(pageNum,pageSize);
        UmsResourceExample example = new UmsResourceExample();
        UmsResourceExample.Criteria criteria = example.createCriteria();
        if(categoryId!=null){
            criteria.andCategoryIdEqualTo(categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            criteria.andNameLike('%'+nameKeyword+'%');
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            criteria.andUrlLike('%'+urlKeyword+'%');
        }
        List<UmsResource> list = resourceMapper.selectByExample(example);
        CommonPage.restPage(list);
        return CommonPage.restPage(list);
    }


    /**
     * 查询资源对应的角色
     * @return
     */
    @Override
    public List<ResourceRoleDTO> getAllResourceRole() {
        return umsResourceMapper.getAllResourceRole();
    }

    @Override
    public UmsResource getById(Long id) {
        return umsResourceMapper.selectByPrimaryKey(id);
    }
    @Override
    public List<UmsResource> list() {
        UmsResourceExample example = new UmsResourceExample();
        return umsResourceMapper.selectByExample(example);
    }

}
