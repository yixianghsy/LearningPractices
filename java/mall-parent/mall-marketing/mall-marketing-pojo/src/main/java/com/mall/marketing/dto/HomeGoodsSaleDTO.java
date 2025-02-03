package com.mall.marketing.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class HomeGoodsSaleDTO  implements Serializable {
    private String categoryName;

    private String pic;


    private String url;


    private List<ProductDTO> productList;

}
