package com.tulingxueyuan.mall.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProductUpdateInitDTO修改的商品初始化数据", description="用于商品修改数据初始化")
public class ProductUpdateInitDTO extends ProductSaveParamsDTO {

    // 一级分类id
    private Long cateParentId;



}
