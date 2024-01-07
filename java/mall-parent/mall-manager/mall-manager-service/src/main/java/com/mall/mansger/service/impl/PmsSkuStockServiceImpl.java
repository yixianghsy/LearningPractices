package com.mall.mansger.service.impl;


import com.mall.mansger.mapper.PmsSkuStockMapper;
import com.mall.mansger.model.PmsSkuStock;
import com.mall.mansger.service.PmsSkuStockService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 商品SKU库存管理Service实现类
 * Created by macro on 2018/4/27.
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        return null;
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return 0;
    }

    @Override
    public PmsSkuStock getById(Long productSkuId) {
        return pmsSkuStockMapper.selectByPrimaryKey(productSkuId);
    }

    @Override
    public int minusUpdate(Integer stock, Integer lowStock, Long id) {
        return pmsSkuStockMapper.minusUpdate(stock, lowStock, id);
    }
}
