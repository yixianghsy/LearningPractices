package com.tulingxueyuan.mall.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商品审核记录
 * </p>
 *
 * @author XuShu
 * @since 2022-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_product_vertify_record")
@ApiModel(value="PmsProductVertifyRecord对象", description="商品审核记录")
public class PmsProductVertifyRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Date createTime;

    @ApiModelProperty(value = "审核人")
    private String vertifyMan;

    private Integer status;

    @ApiModelProperty(value = "反馈详情")
    private String detail;


}
