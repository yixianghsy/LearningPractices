package com.mall.marketing.dto;

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

    private Long attributeId;

    private Long attributeCategoryId;
}
