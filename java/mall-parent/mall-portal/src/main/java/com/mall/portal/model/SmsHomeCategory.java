package com.mall.portal.model;
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
@ApiModel(value="SmsHomeCategory对象", description="")
public class SmsHomeCategory implements Serializable {

    private static final long serialVersionUID=1L;

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
