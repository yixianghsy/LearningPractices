package com.mall.portal.dto;


import com.mall.mansger.model.PmsProductAttributeValue;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SPU的数据传输对象", description="主要用于商品详情展示")
public class PmsProductAttributeValueDTO extends PmsProductAttributeValue {
    private String attrName;
}
