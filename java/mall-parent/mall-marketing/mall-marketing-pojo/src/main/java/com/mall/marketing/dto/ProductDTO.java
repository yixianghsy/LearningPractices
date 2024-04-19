package com.mall.marketing.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class ProductDTO {
    private Long id;
    private String name;
    private String pic;

    private BigDecimal promotionPrice;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String subTitle;

    private Integer sub;

}
