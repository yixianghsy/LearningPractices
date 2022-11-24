package com.tulingxueyuan.mall.modules.pms.mapper;

import com.tulingxueyuan.mall.dto.ProductCateChildrenDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author XuShu
 * @since 2022-11-16
 */
public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategory> {
    /**
     * 获取商品一级分类和二级分类的下拉级联数据
     * @return
     */
    List<ProductCateChildrenDTO> getWithChildren();
}
