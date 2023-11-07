package com.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.mall.content.mapper.CmsSubjectMapper;
import com.mall.content.model.CmsSubject;
import com.mall.content.model.CmsSubjectExample;
import com.mall.mansger.mapper.PmsProductCategoryMapper;
import com.mall.mansger.mapper.PmsProductMapper;
import com.mall.mansger.mapper.SmsHomeAdvertiseMapper;
import com.mall.mansger.model.*;
//import com.mall.portal.dao.HomeDao;
import com.mall.portal.dao.FlashPromotionProductDao;
import com.mall.portal.dao.HomeDao;
import com.mall.portal.domain.FlashPromotionParam;
import com.mall.portal.domain.FlashPromotionProduct;
import com.mall.portal.domain.HomeContentResult;
import com.mall.portal.service.HomeService;
import com.mall.portal.service.SmsFlashPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页内容管理Service实现类
 * Created on 2019/1/28.
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Autowired
    private HomeDao homeDao;
    @Autowired
    private FlashPromotionProductDao flashPromotionProductDao;
    @Autowired
    private SmsFlashPromotionService smsFlashPromotionService;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private CmsSubjectMapper subjectMapper;
    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        //获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取推荐品牌
        result.setBrandList(homeDao.getRecommendBrandList(0,4));
//        //获取秒杀信息
        result.setHomeFlashPromotion(getHomeSecKillProductList());
//        //获取新品推荐
        result.setNewProductList(homeDao.getNewProductList(0,4));
//        //获取人气推荐
        result.setHotProductList(homeDao.getHotProductList(0,4));
//        //获取推荐专题
        result.setSubjectList(homeDao.getRecommendSubjectList(0,4));
        return result;
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO: 2019/1/29 暂时默认推荐所有商品
        PageHelper.startPage(pageNum,pageSize);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1);
        return productMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria()
                .andShowStatusEqualTo(1)
                .andParentIdEqualTo(parentId);
        example.setOrderByClause("sort desc");
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        CmsSubjectExample example = new CmsSubjectExample();
        CmsSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andShowStatusEqualTo(1);
        if(cateId!=null){
            criteria.andCategoryIdEqualTo(cateId);
        }
        return subjectMapper.selectByExample(example);
    }

    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        return null;
    }
    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andTypeEqualTo(1).andStatusEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }
    private List<FlashPromotionProduct> getHomeSecKillProductList(){
        PageHelper.startPage(1,8,"sort desc");
        FlashPromotionParam flashPromotionParam = flashPromotionProductDao.getFlashPromotion(null);
        if(flashPromotionParam==null || CollectionUtils.isEmpty(flashPromotionParam.getRelation())){
            return null;
        }
        List<Long> promotionIds = new ArrayList<>();
        flashPromotionParam.getRelation().stream().forEach(item->{
            promotionIds.add(item.getId());
        });
        PageHelper.clearPage();
        return flashPromotionProductDao.getHomePromotionProductList(promotionIds);
    }
}
