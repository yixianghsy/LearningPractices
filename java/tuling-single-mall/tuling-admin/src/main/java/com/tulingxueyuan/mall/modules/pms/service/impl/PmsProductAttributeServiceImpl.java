package com.tulingxueyuan.mall.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.dto.RelationAttrInfoDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttribute;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductAttributeMapper;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttributeCategory;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductAttributeCategoryService;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@Service
public class PmsProductAttributeServiceImpl extends ServiceImpl<PmsProductAttributeMapper, PmsProductAttribute> implements PmsProductAttributeService {

    @Autowired
    PmsProductAttributeMapper productAttributeMapper;

    @Autowired
    PmsProductAttributeCategoryService productAttrCateService;

    @Override
    public Page list(Long cid, Integer type, Integer pageNum, Integer pageSize) {


        Page page=new Page(pageNum,pageSize);

        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(PmsProductAttribute::getProductAttributeCategoryId,cid)
                .eq(PmsProductAttribute::getType,type)
                .orderByAsc(PmsProductAttribute::getSort);

        return this.page(page,queryWrapper);
    }

    @Override
    public List<RelationAttrInfoDTO> getRelationAttrInfoByCid(Long cId) {
        return productAttributeMapper.getRelationAttrInfoByCid(cId);
    }

    @Override
    @Transactional
    public boolean create(PmsProductAttribute productAttribute) {
        // 1. 保存商品属性
        boolean save = this.save(productAttribute);

        if(save){
            // 2. 更新对应属性、参数的数量
            //      1.先查询商品类型 再更新  2. 直接更新 update  +1

            UpdateWrapper<PmsProductAttributeCategory> updateWrapper = new UpdateWrapper<>();
            // 属性
            if(productAttribute.getType()==0){
                updateWrapper.setSql("attribute_count=attribute_count+1");
                // 用这种方式要先查 updateWrapper.lambda().set(PmsProductAttributeCategory::getAttributeCount,+1)
            }
            // 参数
            else if(productAttribute.getType()==1){
                updateWrapper.setSql("param_count=param_count+1");
            }
            // 根据属性的类型id进行更新
            updateWrapper.lambda().eq(PmsProductAttributeCategory::getId,productAttribute.getProductAttributeCategoryId());
            productAttrCateService.update(updateWrapper);
        }
        return save;
    }

    /**
     * （批量）删除
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean delete(List<Long> ids) {

        if(CollectionUtils.isEmpty(ids)){
            return false;
        }

        // 得到当前属性的类别
        PmsProductAttribute productAttribute=null;
        for (Long id : ids) {
            productAttribute=this.getById(id);
            if(productAttribute!=null){
                break;
            }
        }

        // 1.删除属性  得到删除后的数量
        int length = productAttributeMapper.deleteBatchIds(ids);

        if(length>0 && productAttribute!=null) {
            UpdateWrapper<PmsProductAttributeCategory> updateWrapper = new UpdateWrapper<>();
            // 属性 减
            if(productAttribute.getType()==0){
                updateWrapper.setSql("attribute_count=attribute_count-"+length);
                // 用这种方式要先查 updateWrapper.lambda().set(PmsProductAttributeCategory::getAttributeCount,+1)
            }
            // 参数 减
            else if(productAttribute.getType()==1){
                updateWrapper.setSql("param_count=param_count-"+length);
            }
            // 根据属性的类型id进行更新
            updateWrapper.lambda().eq(PmsProductAttributeCategory::getId,productAttribute.getProductAttributeCategoryId());
            productAttrCateService.update(updateWrapper);
        }
        return length>0;
    }


}