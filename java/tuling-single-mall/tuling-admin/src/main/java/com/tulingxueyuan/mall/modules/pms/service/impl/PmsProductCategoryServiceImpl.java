package com.tulingxueyuan.mall.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductCategoryMapper;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2022-11-16
 */
@Service
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryService {
    /**
     * 获取商品分类列表
     * @param parentId  一级：0
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page list(Long parentId, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        //条件构造器
        QueryWrapper<PmsProductCategory> queryWrapper=new QueryWrapper<>();
       queryWrapper.lambda().eq(PmsProductCategory::getParentId,parentId)
               .orderByAsc(PmsProductCategory::getSort);
        return this.page(page,queryWrapper);
    }
    /**
     * 修改导航栏显示状态
     * @param ids
     * @param navStatus
     * @return
     */
    @Override
    public boolean updateNavStatus(List<Long> ids, Integer navStatus) {
        UpdateWrapper<PmsProductCategory> pmsProductCategoryUpdateWrapper = new UpdateWrapper<>();
        pmsProductCategoryUpdateWrapper.lambda()
                //需要更新得列
                .set(PmsProductCategory::getNavStatus,navStatus)
                //条件
                .in(PmsProductCategory::getId,ids);
        return  this.update(pmsProductCategoryUpdateWrapper);
    }


}
