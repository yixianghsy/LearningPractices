package com.tulingxueyuan.mall.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.dto.RelationAttrInfoDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
public interface PmsProductAttributeService extends IService<PmsProductAttribute> {

    Page list(Long cid, Integer type, Integer pageNum, Integer pageSize);


    List<RelationAttrInfoDTO> getRelationAttrInfoByCid(Long cId);

    boolean create(PmsProductAttribute productAttribute);

    boolean delete(List<Long> ids);
}
