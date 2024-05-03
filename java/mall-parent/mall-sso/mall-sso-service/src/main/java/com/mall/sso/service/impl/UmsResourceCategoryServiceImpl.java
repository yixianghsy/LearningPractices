package com.mall.sso.service.impl;


import com.mall.sso.mapper.UmsResourceCategoryMapper;
import com.mall.sso.model.UmsResourceCategory;
import com.mall.sso.model.UmsResourceCategoryExample;
import com.mall.sso.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理Service实现类
 * Created by macro on 2020/2/5.
 */
@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {
    @Autowired
    private UmsResourceCategoryMapper resourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        UmsResourceCategoryExample example = new UmsResourceCategoryExample();
        example.setOrderByClause("sort desc");
        return resourceCategoryMapper.selectByExample(example);
    }

    @Override
    public boolean create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        int count = resourceCategoryMapper.insert(umsResourceCategory);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean removeById(Long id) {
        int count = resourceCategoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean updateById(UmsResourceCategory umsResourceCategory) {
        int count = resourceCategoryMapper.updateByPrimaryKey(umsResourceCategory);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
}


