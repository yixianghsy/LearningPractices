package com.mall.portal.mapper;

import com.mall.portal.dto.HomeGoodsSaleDTO;

import java.util.List;

public interface SmsHomeCategoryMapper {
    List<HomeGoodsSaleDTO> getHomeCategoryWithProduct();
}
