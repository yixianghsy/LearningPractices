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
 * 首页推荐专题表
 * </p>
 *
 * @author XuShu
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sms_home_recommend_subject")
@ApiModel(value="SmsHomeRecommendSubject对象", description="首页推荐专题表")
public class SmsHomeRecommendSubject implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long subjectId;

    private String subjectName;

    private Integer recommendStatus;

    private Integer sort;


}
