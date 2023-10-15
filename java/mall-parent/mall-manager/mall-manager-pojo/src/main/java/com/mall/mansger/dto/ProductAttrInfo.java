package com.mall.mansger.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品分类对应属性信息
 * Created by macro on 2018/5/23.
 */
@Data
@EqualsAndHashCode
public class ProductAttrInfo implements Serializable {
    @ApiModelProperty("商品属性ID")
    private Long attributeId;
    @ApiModelProperty("商品属性分类ID")
    private Long attributeCategoryId;
}
