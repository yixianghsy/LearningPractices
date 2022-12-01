package com.tulingxueyuan.mall.modules.sms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author XuShu
 * @since 2021-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sms_home_category")
@ApiModel(value="SmsHomeCategory对象", description="")
public class SmsHomeCategory implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    private String categoryName;

    @ApiModelProperty(value = "广告位置：0->PC广告首页；1->app广告轮播")
    private Integer type;

    @ApiModelProperty(value = "上下线状态：0->下线；1->上线")
    private Integer status;

    private String pic;

    @ApiModelProperty(value = "链接地址")
    private String url;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
