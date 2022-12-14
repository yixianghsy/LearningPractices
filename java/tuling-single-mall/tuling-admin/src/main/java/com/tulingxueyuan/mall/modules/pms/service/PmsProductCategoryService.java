package com.tulingxueyuan.mall.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.dto.PmsProductCategoryDTO;
import com.tulingxueyuan.mall.dto.ProductCateChildrenDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
public interface PmsProductCategoryService extends IService<PmsProductCategory> {

    /**
     * 获取商品分类列表
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page list(Long parentId, Integer pageNum, Integer pageSize);

    boolean updateNavStatus(List<Long> ids, Integer navStatus);

    boolean CustomSave(PmsProductCategoryDTO productCategoryDTO);

    boolean update(PmsProductCategoryDTO productCategoryDTO);

    /**
     * 获取商品一级分类和二级分类的下拉级联数据
     * @return
     */
    List<ProductCateChildrenDTO> getWithChildren();
}
