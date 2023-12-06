package com.mall.portal.mapper;

import com.mall.portal.dto.HomeMenusDTO;

import java.util.List;

public interface PmsPortalProductCategoryMapper {
    List<HomeMenusDTO> getProductWithCategory();
}
