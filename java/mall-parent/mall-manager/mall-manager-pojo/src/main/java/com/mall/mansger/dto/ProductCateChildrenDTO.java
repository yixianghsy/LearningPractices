package com.mall.mansger.dto;
import com.mall.mansger.model.PmsProductCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
public class ProductCateChildrenDTO implements Serializable {

    // 商品分类id
    private Long id;

    // 商品分类名称
    private String name;

    // 商品分类二级级联
    private List<PmsProductCategory> children;
}
