package com.mall.mansger.service.impl;


import com.github.pagehelper.PageHelper;
import com.mall.api.CommonPage;
import com.mall.mansger.dto.PmsProductAttributeParam;
import com.mall.mansger.dto.ProductAttrInfo;
import com.mall.mansger.dto.RelationAttrInfoDTO;
import com.mall.mansger.mapper.PmsProductAttributeCategoryMapper;
import com.mall.mansger.mapper.PmsProductAttributeMapper;
import com.mall.mansger.model.PmsProductAttribute;
import com.mall.mansger.model.PmsProductAttributeCategory;
import com.mall.mansger.model.PmsProductAttributeCategoryExample;
import com.mall.mansger.model.PmsProductAttributeExample;
import com.mall.mansger.service.PmsProductAttributeCategoryService;
import com.mall.mansger.service.PmsProductAttributeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 商品属性管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Autowired
    PmsProductAttributeMapper productAttributeMapper;
    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Autowired
    PmsProductAttributeCategoryService productAttrCateService;

    @Override
    public List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public int create(PmsProductAttributeParam pmsProductAttributeParam) {
        return 0;
    }
    // TODO
    @Override
    public Boolean update(PmsProductAttribute productAttribute) {
        return false;
    }
    // TODO
    @Override
    public PmsProductAttribute getById(Long id) {
        return null;
    }

    @Override
    public CommonPage list(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductAttributeExample pmsProductAttributeExample = new PmsProductAttributeExample();
        List<PmsProductAttribute> pmsProductAttributes = productAttributeMapper.selectByExample(pmsProductAttributeExample);
        return CommonPage.restPage(pmsProductAttributes);

    }

    @Override
    public List<RelationAttrInfoDTO> getRelationAttrInfoByCid(Long cId) {
        return productAttributeMapper.getRelationAttrInfoByCid(cId);
    }

    @Override
    public boolean create(PmsProductAttribute productAttribute) {
//        // 1. 保存商品属性
//        boolean save = this.save(productAttribute);
//
//        if(save){
//            // 2. 更新对应属性、参数的数量
//            //      1.先查询商品类型 再更新  2. 直接更新 update  +1
//
//            UpdateWrapper<PmsProductAttributeCategory> updateWrapper = new UpdateWrapper<>();
//            // 属性
//            if(productAttribute.getType()==0){
//                updateWrapper.setSql("attribute_count=attribute_count+1");
//                // 用这种方式要先查 updateWrapper.lambda().set(PmsProductAttributeCategory::getAttributeCount,+1)
//            }
//            // 参数
//            else if(productAttribute.getType()==1){
//                updateWrapper.setSql("param_count=param_count+1");
//            }
//            // 根据属性的类型id进行更新
//            updateWrapper.lambda().eq(PmsProductAttributeCategory::getId,productAttribute.getProductAttributeCategoryId());
//            productAttrCateService.update(updateWrapper);
//        }
//        return save;

        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttribute, pmsProductAttribute);
        int count = productAttributeMapper.insertSelective(pmsProductAttribute);
        //新增商品属性以后需要更新商品属性分类数量
        PmsProductAttributeCategory pmsProductAttributeCategory = productAttributeCategoryMapper.selectByPrimaryKey(pmsProductAttribute.getProductAttributeCategoryId());
        if(pmsProductAttribute.getType()==0){
            pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount()+1);
        }else if(pmsProductAttribute.getType()==1){
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount()+1);
        }
        productAttributeCategoryMapper.updateByPrimaryKey(pmsProductAttributeCategory);

        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }
    /**
     * （批量）删除
     * @param ids
     * @return
     */
    // TODO
    @Override
    public boolean delete(List<Long> ids) {

//        if(CollectionUtils.isEmpty(ids)){
//            return false;
//        }
//
//        // 得到当前属性的类别
//        PmsProductAttribute productAttribute=null;
//        for (Long id : ids) {
//            productAttribute=this.getById(id);
//            if(productAttribute!=null){
//                break;
//            }
//        }
//
//        // 1.删除属性  得到删除后的数量
//        int length = productAttributeMapper.deleteBatchIds(ids);
//
//        if(length>0 && productAttribute!=null) {
//            UpdateWrapper<PmsProductAttributeCategory> updateWrapper = new UpdateWrapper<>();
//            // 属性 减
//            if(productAttribute.getType()==0){
//                updateWrapper.setSql("attribute_count=attribute_count-"+length);
//                // 用这种方式要先查 updateWrapper.lambda().set(PmsProductAttributeCategory::getAttributeCount,+1)
//            }
//            // 参数 减
//            else if(productAttribute.getType()==1){
//                updateWrapper.setSql("param_count=param_count-"+length);
//            }
//            // 根据属性的类型id进行更新
//            updateWrapper.lambda().eq(PmsProductAttributeCategory::getId,productAttribute.getProductAttributeCategoryId());
//            productAttrCateService.update(updateWrapper);
//        }
//        return length>0;

        //获取分类
        PmsProductAttribute pmsProductAttribute = productAttributeMapper.selectByPrimaryKey(ids.get(0));
        Integer type = pmsProductAttribute.getType();
        PmsProductAttributeCategory pmsProductAttributeCategory = productAttributeCategoryMapper.selectByPrimaryKey(pmsProductAttribute.getProductAttributeCategoryId());
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andIdIn(ids);
        int count = productAttributeMapper.deleteByExample(example);
        //删除完成后修改数量
        if(type==0){
            if(pmsProductAttributeCategory.getAttributeCount()>=count){
                pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount()-count);
            }else{
                pmsProductAttributeCategory.setAttributeCount(0);
            }
        }else if(type==1){
            if(pmsProductAttributeCategory.getParamCount()>=count){
                pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount()-count);
            }else{
                pmsProductAttributeCategory.setParamCount(0);
            }
        }
        productAttributeCategoryMapper.updateByPrimaryKey(pmsProductAttributeCategory);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }
    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return null;
    }
}
