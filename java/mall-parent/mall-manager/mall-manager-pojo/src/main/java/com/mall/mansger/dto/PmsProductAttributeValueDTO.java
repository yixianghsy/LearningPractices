package com.mall.mansger.dto;


import com.mall.mansger.model.PmsProductAttributeValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class PmsProductAttributeValueDTO extends PmsProductAttributeValue {
    private String attrName;
}
