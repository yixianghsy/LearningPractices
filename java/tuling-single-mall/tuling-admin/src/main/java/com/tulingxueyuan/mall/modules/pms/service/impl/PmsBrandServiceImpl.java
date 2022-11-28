package com.tulingxueyuan.mall.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.modules.pms.model.PmsBrand;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsBrandMapper;
import com.tulingxueyuan.mall.modules.pms.service.PmsBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@Service
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements PmsBrandService {

    /**
     *
     *  品牌数据列表
     * @param keyword 商品名称
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return
     */
    @Override
    public Page list(String keyword, Integer pageNum, Integer pageSize) {

        Page page=new Page(pageNum,pageSize);

        QueryWrapper<PmsBrand> pmsBrandQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            pmsBrandQueryWrapper.lambda().like(PmsBrand::getName,keyword);
        }
        pmsBrandQueryWrapper.lambda().orderByAsc(PmsBrand::getSort);

        return this.page(page,pmsBrandQueryWrapper);
    }
}
