package com.tulingxueyuan.mall.modules.sms.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 优惠券和产品的关系表
 * </p>
 *
 * @author XuShu
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sms_coupon_product_relation")
@ApiModel(value="SmsCouponProductRelation对象", description="优惠券和产品的关系表")
public class SmsCouponProductRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long couponId;

    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品编码")
    private String productSn;


}
