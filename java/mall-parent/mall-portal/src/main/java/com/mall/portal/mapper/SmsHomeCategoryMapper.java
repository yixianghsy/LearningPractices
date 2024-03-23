package com.mall.portal.mapper;

import com.mall.marketing.dto.HomeGoodsSaleDTO;

import java.util.List;

public interface SmsHomeCategoryMapper {
    List<HomeGoodsSaleDTO> getHomeCategoryWithProduct();
}
