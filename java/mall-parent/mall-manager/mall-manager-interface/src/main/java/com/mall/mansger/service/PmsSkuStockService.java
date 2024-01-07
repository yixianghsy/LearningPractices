package com.mall.mansger.service;

import com.mall.mansger.model.PmsSkuStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品SKU库存管理Service
 * Created by macro on 2018/4/27.
 */

public interface PmsSkuStockService {
    /**
     * 根据产品id和skuCode关键字模糊搜索
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);

    PmsSkuStock getById(Long productSkuId);

    int minusUpdate ( Integer stock,Integer lowStock ,  Long id);
}
