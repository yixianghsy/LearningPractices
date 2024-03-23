package com.mall.mansger.mapper;
import com.mall.mansger.dto.HomeMenusDTO;

import java.util.List;

public interface PmsPortalProductCategoryMapper {
    List<HomeMenusDTO> getProductWithCategory();
}
