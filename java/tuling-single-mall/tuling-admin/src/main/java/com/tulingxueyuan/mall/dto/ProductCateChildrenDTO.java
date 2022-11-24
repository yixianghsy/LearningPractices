package com.tulingxueyuan.mall.dto;

import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
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
@ApiModel(value="ProductCateChildrenDTO商品一级分类和二级分类的级联传输对象",
        description="用于商品列表--商品分类下拉级联数据")
public class ProductCateChildrenDTO {

    // 商品分类id
    private Long id;

    // 商品分类名称
    private String name;

    // 商品分类二级级联
    private List<PmsProductCategory> children;
}
