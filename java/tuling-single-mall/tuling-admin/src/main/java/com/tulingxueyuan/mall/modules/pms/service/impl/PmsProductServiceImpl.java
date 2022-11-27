package com.tulingxueyuan.mall.modules.pms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.dto.ProductConditionDTO;
import com.tulingxueyuan.mall.dto.ProductSaveParamsDTO;
import com.tulingxueyuan.mall.dto.ProductUpdateInitDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProduct;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductMapper;
import com.tulingxueyuan.mall.modules.pms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2022-11-16
 */
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {
    @Autowired
    PmsProductMapper productMapper;

    @Autowired
    PmsMemberPriceService memberPriceService;
    @Autowired
    PmsProductLadderService productLadderService;
    @Autowired
    PmsProductFullReductionService productFullReductionService;
    @Autowired
    PmsSkuStockService skuStockService;
    @Autowired
    PmsProductAttributeValueService productAttributeValueService;

    @Override
    public Page list(ProductConditionDTO condition) {
        Page page = new Page(condition.getPageNum(),condition.getPageSize());
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmsProduct> lambdaWrapper = queryWrapper.lambda();
        // 商品名称
        if(!StrUtil.isBlank(condition.getKeyword())){
            lambdaWrapper.like(PmsProduct::getName,condition.getKeyword());
        }
        // 商品货号
        if(!StrUtil.isBlank(condition.getProductSn())){
            lambdaWrapper.eq(PmsProduct::getProductSn,condition.getProductSn());
        }

        // 商品分类
        if(condition.getProductCategoryId()!=null && condition.getProductCategoryId()>0){
            lambdaWrapper.eq(PmsProduct::getProductCategoryId,condition.getProductCategoryId());
        }
        // 商品品牌
        if(condition.getBrandId()!=null && condition.getBrandId()>0){
            lambdaWrapper.eq(PmsProduct::getBrandId,condition.getBrandId());
        }
        // 上架状态
        if(condition.getPublishStatus()!=null){
            lambdaWrapper.eq(PmsProduct::getPublishStatus,condition.getPublishStatus());
        }
        // 审核状态
        if(condition.getVerifyStatus()!=null){
            lambdaWrapper.eq(PmsProduct::getVerifyStatus,condition.getVerifyStatus());
        }
        lambdaWrapper.orderByAsc(PmsProduct::getSort);
        return this.page(page,lambdaWrapper);
    }

    @Override
    public boolean updateStatus(Integer publishStatus, List<Long> ids, SFunction<PmsProduct, ?> getPublishStatus) {
        return false;
    }

    @Override
    public boolean create(ProductSaveParamsDTO productSaveParamsDTO) {
        return false;
    }

    @Override
    public ProductUpdateInitDTO getUpdateInfo(Long id) {
        return null;
    }

    @Override
    public boolean update(ProductSaveParamsDTO productSaveParamsDTO) {
        return false;
    }
}
