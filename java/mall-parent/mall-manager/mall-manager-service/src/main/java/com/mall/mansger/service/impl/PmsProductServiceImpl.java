package com.mall.mansger.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.mall.api.CommonPage;
import com.mall.mansger.dto.*;
import com.mall.mansger.mapper.PmsProductMapper;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.model.PmsProductExample;
import com.mall.mansger.service.PmsProductService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 商品管理Service实现类
 * Created by macro on 2018/4/26.
 */
// TODO
@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    private PmsProductMapper productMapper;
    @Override
    public int create(PmsProductParam productParam) {
        return 0;
    }
    // TODO
    @Override
    public CommonPage list(ProductConditionDTO condition) {
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (condition.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(condition.getPublishStatus());
        }
        if (condition.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(condition.getVerifyStatus());
        }
        if (!StrUtil.isEmpty(condition.getKeyword())) {
            criteria.andNameLike("%" + condition.getKeyword() + "%");
        }
        if (!StrUtil.isEmpty(condition.getProductSn())) {
            criteria.andProductSnEqualTo(condition.getProductSn());
        }
        if (condition.getBrandId() != null) {
            criteria.andBrandIdEqualTo(condition.getBrandId());
        }
        if (condition.getProductCategoryId() != null) {
            criteria.andProductCategoryIdEqualTo(condition.getProductCategoryId());
        }
        List<PmsProduct> pmsProducts = productMapper.selectByExample(productExample);
        return CommonPage.restPage(pmsProducts);
    }
    // TODO
    @Override
    public boolean updateStatus(Integer publishStatus, List<Long> ids, Integer newStatus) {
        return false;
    }



    @Override
    public boolean create(ProductSaveParamsDTO productSaveParamsDTO) {
//        // 1. 保存商品基本信息 --商品主表
//        PmsProduct product=productSaveParamsDTO;
//        product.setId(null);
//        boolean result = this.save(product);
//        if(result) {
//
//            // 为了解决 前端会传入其他促销方式的空数据进来
//            switch (product.getPromotionType()) {
//                case 2:
//                    // 2. 会员价格
//                    SaveManyList(productSaveParamsDTO.getMemberPriceList(), product.getId(), memberPriceService);
//                    break;
//                case 3:
//                    // 3. 阶梯价格
//                    SaveManyList(productSaveParamsDTO.getProductLadderList(), product.getId(), productLadderService);
//                    break;
//                case 4:
//                    // 4. 减满价格
//                    SaveManyList(productSaveParamsDTO.getProductFullReductionList(), product.getId(), productFullReductionService);
//                    break;
//            }
//            // 5. sku
//            SaveManyList(productSaveParamsDTO.getSkuStockList(),product.getId(), skuStockService);
//
//            // 6 spu
//            SaveManyList(productSaveParamsDTO.getProductAttributeValueList(),product.getId(), productAttributeValueService);
//
//        }
//        return result;
        return false;
    }
    /**
     * 编辑数据初始化
     * @param id
     * @return
     */
    @Override
    public ProductUpdateInitDTO getUpdateInfo(Long id) {
        return productMapper.getUpdateInfo(id);
    }
    /**
     * 修改保存
     * @param productSaveParamsDTO
     * @return
     */
    @Override
    public boolean update(ProductSaveParamsDTO productSaveParamsDTO) {
//        // 1. 保存商品基本信息 --商品主表
//        PmsProduct product=productSaveParamsDTO;
//        boolean result = this.updateById(product);
//        if(result) {
//
//            // 为了解决 前端会传入其他促销方式的空数据进来
//            switch (product.getPromotionType()) {
//                case 2:
//                    // 2. 会员价格
//
//                    // 根据商品id删除
//                    DeleteManyListByProductId(product.getId(),memberPriceService);
//                    SaveManyList(productSaveParamsDTO.getMemberPriceList(), product.getId(), memberPriceService);
//                    break;
//                case 3:
//                    // 根据商品id删除
//                    DeleteManyListByProductId(product.getId(),productLadderService);
//                    // 3. 阶梯价格
//                    SaveManyList(productSaveParamsDTO.getProductLadderList(), product.getId(), productLadderService);
//                    break;
//                case 4:
//                    // 根据商品id删除
//                    DeleteManyListByProductId(product.getId(),productFullReductionService);
//                    // 4. 减满价格
//                    SaveManyList(productSaveParamsDTO.getProductFullReductionList(), product.getId(), productFullReductionService);
//                    break;
//            }
//            // 根据商品id删除
//            DeleteManyListByProductId(product.getId(),skuStockService);
//            // 5. sku
//            SaveManyList(productSaveParamsDTO.getSkuStockList(),product.getId(), skuStockService);
//
//            // 根据商品id删除
//            DeleteManyListByProductId(product.getId(),productAttributeValueService);
//            // 6 spu
//            SaveManyList(productSaveParamsDTO.getProductAttributeValueList(),product.getId(), productAttributeValueService);
//
//        }
//        return result;
        return false;
    }



    @Override
    public CommonPage list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (productQueryParam.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }
        if (!StrUtil.isEmpty(productQueryParam.getKeyword())) {
            criteria.andNameLike("%" + productQueryParam.getKeyword() + "%");
        }
        if (!StrUtil.isEmpty(productQueryParam.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }
        List<PmsProduct> pmsProducts = productMapper.selectByExample(productExample);
        return CommonPage.restPage(pmsProducts);
    }


    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        return 0;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        return 0;
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return 0;
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        return 0;
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        return 0;
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        return null;
    }
    // TODO
    @Override
    public boolean removeByIds(List<Long> ids) {
        return false;
    }


//    /**
//     * 根据商品id删除关联数据
//     */
//    public void DeleteManyListByProductId(Long productId, IService service){
//
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("product_id",productId);
//
//        service.remove(queryWrapper);
//    }
//
//
//    /**
//     *  公共方法： 保存会员价格、阶梯价格、减满价格、 sku 、 spu 商品的关联数据
//     *
//     *  统一： 都需要设置商品id,  都需要批量保存
//     */
//    public void SaveManyList(List list, Long productId, IService service){
//        // 如果数据为空 或者长度为0  不做任何操作
//        if(CollectionUtil.isEmpty(list)) return;
//
//        try {
//            // 循环 反射 赋值商品id
//            for (Object obj : list) {
//                Method setProductIdMethod = obj.getClass().getMethod("setProductId", Long.class);
//
//                // 在修改状态清除主键id
//                Method setId = obj.getClass().getMethod("setId", Long.class);
//                setId.invoke(obj,(Long)null);
//
//                // 调用setProductId
//                setProductIdMethod.invoke(obj, productId);
//            }
//
//            service.saveBatch(list);
//        }
//        catch (Exception ex){
//            throw new RuntimeException(ex);
//        }
//    }
}
