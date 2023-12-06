package com.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.mall.portal.domain.PmsProductParam;
import com.mall.portal.dao.FlashPromotionProductDao;
import com.mall.portal.dao.PortalProductDao;
import com.mall.portal.domain.FlashPromotionProduct;
import com.mall.portal.service.PmsProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    private PortalProductDao portalProductDao;

    @Autowired
    private FlashPromotionProductDao flashPromotionProductDao;
    /**
     * add by yangguo
     * 获取商品详情信息
     * @param id 产品ID
     */
    @Override
    public PmsProductParam getProductInfo(Long id,Long flashPromotionId,Long flashPromotionSessionId){
        return portalProductDao.getProductInfo(id,flashPromotionId,flashPromotionSessionId);
    }

    /**
     * add by yangguo
     * 获取秒杀商品列表
     * @param flashPromotionId 秒杀活动ID，关联秒杀活动设置
     * @param sessionId 场次活动ID，for example：13:00-14:00场等
     */
    @Override
    public List<FlashPromotionProduct> getFlashProductList(Integer pageSize,Integer pageNum,Long flashPromotionId, Long sessionId){
        PageHelper.startPage(pageNum,pageSize,"sort desc");
        return flashPromotionProductDao.getFlashProductList(flashPromotionId,sessionId);
    }
}

