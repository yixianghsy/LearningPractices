package com.mall.mansger.service.impl;


import com.mall.mansger.model.PmsSkuStock;
import com.mall.mansger.service.PmsSkuStockService;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

/**
 * 商品SKU库存管理Service实现类
 * Created by macro on 2018/4/27.
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        return null;
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return 0;
    }
}
