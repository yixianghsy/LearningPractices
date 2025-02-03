package com.mall.portal.dto;
import com.mall.mansger.dto.HomeMenusDTO;
import com.mall.marketing.model.SmsHomeAdvertise;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HomeMenusBannerDTO {

    private List<HomeMenusDTO> homeMenusList;
    private List<SmsHomeAdvertise> homeAdvertisesList;
}
