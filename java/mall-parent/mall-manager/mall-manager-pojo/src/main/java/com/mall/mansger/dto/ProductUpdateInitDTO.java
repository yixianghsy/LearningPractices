package com.mall.mansger.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
public class ProductUpdateInitDTO extends ProductSaveParamsDTO {

    // 一级分类id
    private Long cateParentId;



}
