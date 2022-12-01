package com.tulingxueyuan.mall.modules.ums.model;

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
 * 会员登录记录
 * </p>
 *
 * @author XuShu
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_member_login_log")
@ApiModel(value="UmsMemberLoginLog对象", description="会员登录记录")
public class UmsMemberLoginLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Date createTime;

    private String ip;

    private String city;

    @ApiModelProperty(value = "登录类型：0->PC；1->android;2->ios;3->小程序")
    private Integer loginType;

    private String province;


}
