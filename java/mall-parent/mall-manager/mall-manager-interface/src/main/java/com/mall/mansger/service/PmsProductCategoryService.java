package com.mall.mansger.service;

import com.mall.api.CommonPage;
import com.mall.mansger.PmsProductCategoryDTO;
import com.mall.mansger.dto.HomeMenusDTO;
import com.mall.mansger.dto.PmsProductCategoryParam;
import com.mall.mansger.dto.PmsProductCategoryWithChildrenItem;
import com.mall.mansger.dto.ProductCateChildrenDTO;
import com.mall.mansger.model.PmsProductCategory;


import java.util.List;

/**
 * 商品分类管理Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductCategoryService {

    /**
     * 获取商品分类列表
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    CommonPage list(Long parentId, Integer pageNum, Integer pageSize);
    /**
     * 批量修改导航状态
     */

    boolean updateNavStatus(List<Long> ids, Integer navStatus);

    boolean CustomSave(PmsProductCategoryDTO productCategoryDTO);

    boolean update(PmsProductCategoryDTO productCategoryDTO);

    /**
     * 获取商品一级分类和二级分类的下拉级联数据
     * @return
     */
    List<ProductCateChildrenDTO> getWithChildren();
    /**
     * 创建商品分类
     */

    int create(PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 修改商品分类
     */

    int update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 分页获取商品分类
     */
    List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 删除商品分类
     */
    boolean delete(Long id);

    /**
     * 根据ID获取商品分类
     */
    PmsProductCategory getItem(Long id);



    /**
     * 批量修改显示状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 以层级形式获取商品分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    List<HomeMenusDTO> getMenus();

}
