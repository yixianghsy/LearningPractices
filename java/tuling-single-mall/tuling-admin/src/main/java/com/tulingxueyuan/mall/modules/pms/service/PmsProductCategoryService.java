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
 * @since 2022-11-16
 */
public interface PmsProductCategoryService extends IService<PmsProductCategory> {

    Page list(Long parentId, Integer pageNum, Integer pageSize);

    boolean updateNavStatus(List<Long> ids, Integer navStatus);

    boolean CustomSave(PmsProductCategoryDTO productCategoryDTO);

    boolean update(PmsProductCategoryDTO productCategoryDTO);


    List<ProductCateChildrenDTO> getWithChildren();
}
