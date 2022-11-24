package com.tulingxueyuan.mall.dto;

import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttribute;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProductAttributeCateDTO筛选属性数据传输对象", description="用于商品分类--筛选属性下拉级联数据")
public class ProductAttributeCateDTO {

    // 商品类型id
    private Long id;

    // 商品类型名称
    private String name;

    // 商品属性二级级联
    private List<PmsProductAttribute> productAttributeList;
}
