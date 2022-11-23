package com.tulingxueyuan.mall.dto;

import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="商品分类的数据传输对象", description="用于商品分类添加、修改数据接收")
public class PmsProductCategoryDTO extends PmsProductCategory {
    private List<Long> productAttributeIdList;
}
