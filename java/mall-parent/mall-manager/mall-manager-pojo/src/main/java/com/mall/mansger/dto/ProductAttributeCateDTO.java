package com.mall.mansger.dto;

import com.mall.mansger.model.PmsProductAttribute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
public class ProductAttributeCateDTO implements Serializable {

    // 商品类型id
    private Long id;

    // 商品类型名称
    private String name;

    // 商品属性二级级联
    private List<PmsProductAttribute> productAttributeList;
}
