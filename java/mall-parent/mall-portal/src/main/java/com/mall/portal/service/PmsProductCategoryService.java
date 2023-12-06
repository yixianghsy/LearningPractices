package com.mall.portal.service;

import com.mall.portal.dto.HomeMenusDTO;

import java.util.List;
/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-14
 */
public interface PmsProductCategoryService {
    List<HomeMenusDTO> getMenus();
}
