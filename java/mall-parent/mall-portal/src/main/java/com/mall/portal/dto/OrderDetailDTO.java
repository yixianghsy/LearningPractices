package com.mall.portal.dto;

import com.mall.order.model.OmsOrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="订单详情数据传输对象", description="订单详情数据传输对象")
public class OrderDetailDTO {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    @ApiModelProperty(value = "城市")
    private String receiverCity;

    @ApiModelProperty(value = "区")
    private String receiverRegion;

    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;

    @ApiModelProperty(value = "应付金额（实际支付金额）")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单详细商品列表")
    private List<OmsOrderItem> orderItemList;

    @ApiModelProperty(value = "提交时间")
    private Date createTime;
    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;

    @ApiModelProperty(value = "正常订单超时时间(分)")
    private Integer normalOrderOvertime;
}
