package com.mall.mansger.service.impl;


import com.github.pagehelper.PageHelper;
import com.mall.mansger.dto.PmsProductAttributeCategoryItem;
import com.mall.mansger.mapper.PmsProductAttributeCategoryMapper;
import com.mall.mansger.model.PmsProductAttributeCategory;
import com.mall.mansger.model.PmsProductAttributeCategoryExample;
import com.mall.mansger.service.PmsProductAttributeCategoryService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 商品属性分类管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {
    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Override
    public int create(String name) {
        return 0;
    }

    @Override
    public int update(Long id, String name) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return null;
    }

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return productAttributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return null;
    }
}
