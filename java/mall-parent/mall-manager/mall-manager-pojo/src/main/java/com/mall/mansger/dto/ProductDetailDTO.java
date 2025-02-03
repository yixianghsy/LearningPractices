package com.mall.mansger.dto;

import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.model.PmsSkuStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class ProductDetailDTO extends PmsProduct {

    // 商品属性相关

    private List<PmsProductAttributeValueDTO> productAttributeValueList;
    // 商品sku库存信息
    private List<PmsSkuStock> skuStockList;
}
