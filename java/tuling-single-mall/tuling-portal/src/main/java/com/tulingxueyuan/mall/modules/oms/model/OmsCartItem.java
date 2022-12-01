package com.tulingxueyuan.mall.modules.oms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author XuShu
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_cart_item")
@ApiModel(value="OmsCartItem对象", description="购物车表")
public class OmsCartItem implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Long productSkuId;

    private Long memberId;

    @ApiModelProperty(value = "购买数量")
    private Integer quantity;

    @ApiModelProperty(value = "添加到购物车的价格")
    private BigDecimal price;

    @ApiModelProperty(value = "销售属性1")
    private String sp1;

    @ApiModelProperty(value = "销售属性2")
    private String sp2;

    @ApiModelProperty(value = "销售属性3")
    private String sp3;

    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品副标题（卖点）")
    private String productSubTitle;

    @ApiModelProperty(value = "商品sku条码")
    private String productSkuCode;

    @ApiModelProperty(value = "会员昵称")
    private String memberNickname;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "修改时间")
    private Date modifyDate;

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    @TableLogic(value="0",delval = "1")
    private Integer deleteStatus;

    @ApiModelProperty(value = "商品分类")
    private Long productCategoryId;

    private String productBrand;

    private String productSn;

    //@ApiModelProperty(value = "商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]")
    private String productAttr;


}
