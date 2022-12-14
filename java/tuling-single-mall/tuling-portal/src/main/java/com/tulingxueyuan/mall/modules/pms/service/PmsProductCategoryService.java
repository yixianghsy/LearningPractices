package com.tulingxueyuan.mall.modules.pms.service;

import com.tulingxueyuan.mall.dto.HomeMenusDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-14
 */
public interface PmsProductCategoryService extends IService<PmsProductCategory> {

    List<HomeMenusDTO> getMenus();
}
