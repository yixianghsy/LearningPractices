package com.mall.mansger.service.impl;


import com.mall.mansger.dto.PmsProductAttributeParam;
import com.mall.mansger.dto.ProductAttrInfo;
import com.mall.mansger.model.PmsProductAttribute;
import com.mall.mansger.service.PmsProductAttributeService;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

/**
 * 商品属性管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Override
    public List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public int create(PmsProductAttributeParam pmsProductAttributeParam) {
        return 0;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        return 0;
    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return null;
    }

    @Override
    public int delete(List<Long> ids) {
        return 0;
    }

    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return null;
    }
}
