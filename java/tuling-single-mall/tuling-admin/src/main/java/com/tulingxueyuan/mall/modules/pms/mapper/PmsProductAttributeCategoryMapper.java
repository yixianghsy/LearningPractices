package com.tulingxueyuan.mall.modules.pms.mapper;

import com.tulingxueyuan.mall.dto.ProductAttributeCateDTO;
import com.tulingxueyuan.mall.dto.ProductCateChildrenDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 Mapper 接口
 * </p>
 *
 * @author XuShu
 * @since 2022-11-16
 */
public interface PmsProductAttributeCategoryMapper extends BaseMapper<PmsProductAttributeCategory> {
    List<ProductAttributeCateDTO> getListWithAttr();
}
