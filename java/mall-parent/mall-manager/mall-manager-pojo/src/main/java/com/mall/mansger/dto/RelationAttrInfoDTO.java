package com.mall.mansger.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
public class RelationAttrInfoDTO implements Serializable {

    // 商品类型id
    private Long attributeCategoryId;

    // 商品属性Id
    private  Long attributeId;
}
