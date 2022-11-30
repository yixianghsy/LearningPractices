package com.tulingxueyuan.mall.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="首页类型导航栏数据", description="首页类型导航栏数据")
public class HomeMenusDTO {

    private Long id;
    private String name;

    private List<ProductDTO> productList;
}
