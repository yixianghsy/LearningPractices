package com.mall.mansger.service.impl;


import com.github.pagehelper.PageHelper;
import com.mall.api.CommonPage;
import com.mall.mansger.PmsProductCategoryDTO;
import com.mall.mansger.dto.HomeMenusDTO;
import com.mall.mansger.dto.PmsProductCategoryParam;
import com.mall.mansger.dto.PmsProductCategoryWithChildrenItem;
import com.mall.mansger.dto.ProductCateChildrenDTO;
import com.mall.mansger.mapper.PmsPortalProductCategoryMapper;
import com.mall.mansger.mapper.PmsProductCategoryDao;
import com.mall.mansger.mapper.PmsProductCategoryMapper;
import com.mall.mansger.model.PmsProductCategory;
import com.mall.mansger.model.PmsProductCategoryAttributeRelation;
import com.mall.mansger.model.PmsProductCategoryExample;
import com.mall.mansger.service.PmsProductCategoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类管理Service实现类
 * Created by macro on 2018/4/26.
 */
// TODO
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Autowired
    private PmsPortalProductCategoryMapper mapper;
    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private PmsProductCategoryDao productCategoryDao;

    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {

        return 0;
    }

    @Override
    public int update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        // 保存商品分类
        PmsProductCategory productCategory = new PmsProductCategory();
        // 通过BeanUtils 将productCategoryDTO的数据拷贝到productCategory
        // 为什么要拷贝：因为一定要通过this.save 去保存PmsProductCategory，因为只有它才映射了@TableName
//        BeanUtils.copyProperties(productCategoryDTO, productCategory);
//        if (productCategory.getParentId() == 0) {
//            productCategory.setLevel(0);
//        } else {
//            // 如果有多级分类，根据parentId查出商品分类获取level+1
//            // 由于只有2级分类，直接设置为1
//            productCategory.setLevel(1);
//        }
//        this.updateById(productCategory);
//
//
//        // 删除已保存的关联属性—根据商品分类id删除
//        QueryWrapper<PmsProductCategoryAttributeRelation> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(PmsProductCategoryAttributeRelation::getProductCategoryId, productCategory.getId());
//        relationService.remove(queryWrapper);
//
//        saveAttrRelation(productCategoryDTO, productCategory);
//        return true;
        return 0;
    }

    @Override
    public List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return productCategoryMapper.selectByExample(example);
    }
    // TODO
    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return null;
    }

    @Override
    public CommonPage list(Long parentId, Integer pageNum, Integer pageSize) {
//        Page page=new Page(pageNum,pageSize);
//
//        // 条件构造器
//        QueryWrapper<PmsProductCategory> queryWrapper=new QueryWrapper<>();
//        //queryWrapper.eq("parent_id",parentId);
//        queryWrapper.lambda().eq(PmsProductCategory::getParentId,parentId)
//                .orderByAsc(PmsProductCategory::getSort);
//
//        return this.page(page, queryWrapper);
        return null;
    }
    // TODO
    @Override
    public boolean updateNavStatus(List<Long> ids, Integer navStatus) {
//        UpdateWrapper<PmsProductCategory> pmsProductCategoryUpdateWrapper = new UpdateWrapper<>();
//
//        pmsProductCategoryUpdateWrapper.lambda()
//                // 需要更新的列
//                .set(PmsProductCategory::getNavStatus,navStatus)
//                // 条件
//                .in(PmsProductCategory::getId,ids);
//
//        return this.update(pmsProductCategoryUpdateWrapper);
        return false;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean CustomSave(PmsProductCategoryDTO productCategoryDTO) {
//        // 保存商品分类
//        PmsProductCategory productCategory=new PmsProductCategory();
//        // 通过BeanUtils 将productCategoryDTO的数据拷贝到productCategory
//        // 为什么要拷贝：因为一定要通过this.save 去保存PmsProductCategory，因为只有它才映射了@TableName
//        BeanUtils.copyProperties(productCategoryDTO,productCategory);
//        // 由于商品数量 和级别 在表单中没有维护， 需要设置默认值
//        productCategory.setProductCount(0);
//        if(productCategory.getParentId()==0){
//            productCategory.setLevel(0);
//        }
//        else {
//            // 如果有多级分类，根据parentId查出商品分类获取level+1
//            // 由于只有2级分类，直接设置为1
//            productCategory.setLevel(1);
//        }
//        this.save(productCategory);
//        saveAttrRelation(productCategoryDTO, productCategory);

        return false;
    }

    @Override
    public boolean update(PmsProductCategoryDTO productCategoryDTO) {
        return false;
    }

    @Override
    public List<ProductCateChildrenDTO> getWithChildren() {
        return productCategoryMapper.getWithChildren();
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        return 0;
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return productCategoryDao.listWithChildren();
    }

    @Override
    public List<HomeMenusDTO> getMenus() {
        return mapper.getProductWithCategory();
    }
    /**
     * 添加关联属性
     * @param productCategoryDTO
     * @param productCategory
     * @return
     */
//    private boolean saveAttrRelation(PmsProductCategoryDTO productCategoryDTO, PmsProductCategory productCategory) {
//
//        List<Long> productAttributeIdList = productCategoryDTO.getProductAttributeIdList();
//        List<PmsProductCategoryAttributeRelation> list=new ArrayList<>();
//        for (Long attrId : productAttributeIdList) {
//            // 得到分类保存后的主键id,   保存商品分类筛选属性关系
//            PmsProductCategoryAttributeRelation productCategoryAttributeRelation=new PmsProductCategoryAttributeRelation();
//            productCategoryAttributeRelation.setProductCategoryId(productCategory.getId());
//            productCategoryAttributeRelation.setProductAttributeId(attrId);
//            list.add(productCategoryAttributeRelation);
//
//        }
//        return relationService.saveBatch(list);
//    }
    /**
     * 根据分类的parentId设置分类的level
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        //没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            //有父分类时选择根据父分类level设置
            PmsProductCategory parentCategory = productCategoryMapper.selectByPrimaryKey(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }

}
