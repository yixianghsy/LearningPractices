package com.mall.portal.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="添加购物车参数接收对象", description="添加购物车参数接收对象")
public class AddCarDTO {
    /**
     *
     *           productId: this.id,
     *           productSkuId: this.skuId,
     *           quantity: 1,
     */
    private Long productId;
    private Long productSkuId;
    private Integer quantity;
}
