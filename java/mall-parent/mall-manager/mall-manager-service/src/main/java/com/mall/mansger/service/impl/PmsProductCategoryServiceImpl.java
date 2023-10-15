package com.mall.mansger.service.impl;


import com.github.pagehelper.PageHelper;
import com.mall.mansger.dto.PmsProductCategoryParam;
import com.mall.mansger.dto.PmsProductCategoryWithChildrenItem;
import com.mall.mansger.mapper.PmsProductCategoryDao;
import com.mall.mansger.mapper.PmsProductCategoryMapper;
import com.mall.mansger.model.PmsProductCategory;
import com.mall.mansger.model.PmsProductCategoryExample;
import com.mall.mansger.service.PmsProductCategoryService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {
    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private PmsProductCategoryDao productCategoryDao;
    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {
        return 0;
    }

    @Override
    public int update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        return 0;
    }

    @Override
    public List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return null;
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        return 0;
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        return 0;
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return productCategoryDao.listWithChildren();
    }
}
