package com.mall.portal.service;
import com.mall.mansger.mapper.PmsProductCategoryMapper;
import com.mall.portal.dto.HomeMenusDTO;
import com.mall.portal.mapper.PmsPortalProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-14
 */
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {
    @Autowired
    PmsPortalProductCategoryMapper mapper;
    /**
     * 获取首页类型导航菜单
     * @return
     */
    @Override
    public List<HomeMenusDTO> getMenus() {
        return mapper.getProductWithCategory();
    }
}
