package com.mall.mansger.service;

import com.mall.api.CommonPage;
import com.mall.mansger.dto.*;
import com.mall.mansger.model.PmsProduct;
import java.util.List;

/**
 * 商品管理Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductService {

    CommonPage list(ProductConditionDTO condition);

    /**
     * 更新 单个字段的公共方法
     * @param publishStatus
     * @param ids
     * @param getPublishStatus
     * @return
     */
//    boolean updateStatus(Integer publishStatus, List<Long> ids, SFunction<PmsProduct, ?> getPublishStatus);
    boolean updateStatus(Integer publishStatus, List<Long> ids ,Integer newStatus);
    boolean create(ProductSaveParamsDTO productSaveParamsDTO);

    /**
     * 根据商品编号获取更新信息
     */

    ProductUpdateInitDTO getUpdateInfo(Long id);

    boolean update(ProductSaveParamsDTO productSaveParamsDTO);
    /**
     * 创建商品
     */
    int create(PmsProductParam productParam);


    /**
     * 分页查询商品
     */
    CommonPage list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);


    /**
     * 批量修改审核状态
     * @param ids 产品id
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     */
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     */
    List<PmsProduct> list(String keyword);

    boolean removeByIds(List<Long> ids);
}
