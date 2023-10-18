package com.mall.sso.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.mall.sso.mapper.UmsAdminMapper;
import com.mall.sso.mapper.UmsResourceMapper;
import com.mall.sso.model.UmsResource;
import com.mall.sso.model.UmsResourceExample;
import com.mall.sso.service.UmsAdminCacheService;
import com.mall.sso.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    private UmsResourceMapper resourceMapper;
    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminCacheService adminCacheService;
    @Override
    public int create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        return resourceMapper.insert(umsResource);
    }

    @Override
    public int update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        int count = resourceMapper.updateByPrimaryKeySelective(umsResource);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public UmsResource getItem(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        int count = resourceMapper.deleteByPrimaryKey(id);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
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
        return list;
    }
    @Override
    public List<UmsResource> listAll() {
        return resourceMapper.selectByExample(new UmsResourceExample());
    }
}
