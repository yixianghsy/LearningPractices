package com.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.mall.content.model.CmsSubject;
import com.mall.mansger.mapper.SmsHomeAdvertiseMapper;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.model.PmsProductCategory;
import com.mall.mansger.model.SmsHomeAdvertise;
import com.mall.mansger.model.SmsHomeAdvertiseExample;
//import com.mall.portal.dao.HomeDao;
import com.mall.portal.dao.FlashPromotionProductDao;
import com.mall.portal.dao.HomeDao;
import com.mall.portal.domain.FlashPromotionParam;
import com.mall.portal.domain.FlashPromotionProduct;
import com.mall.portal.domain.HomeContentResult;
import com.mall.portal.service.HomeService;
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
        return null;
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        return null;
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        return null;
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
