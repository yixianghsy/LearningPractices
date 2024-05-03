package com.mall.mansger.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文件上传返回结果
 * Created by macro on 2019/12/25.
 */
@Data
@EqualsAndHashCode
public class MinioUploadDto implements Serializable {
    @ApiModelProperty("文件访问URL")
    private String url;
    @ApiModelProperty("文件名称")
    private String name;
}
