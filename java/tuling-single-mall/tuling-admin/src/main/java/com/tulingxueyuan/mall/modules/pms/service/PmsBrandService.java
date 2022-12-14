package com.tulingxueyuan.mall.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.modules.pms.model.PmsBrand;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
public interface PmsBrandService extends IService<PmsBrand> {
    /**
     *
     *  品牌数据列表
     * @param keyword 商品名称
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return
     */
    Page list(String keyword, Integer pageNum, Integer pageSize);
}
