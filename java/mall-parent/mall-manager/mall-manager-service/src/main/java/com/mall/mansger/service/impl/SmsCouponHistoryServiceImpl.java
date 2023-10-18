package com.mall.mansger.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.mall.mansger.mapper.SmsCouponHistoryMapper;
import com.mall.mansger.model.SmsCouponHistory;
import com.mall.mansger.model.SmsCouponHistoryExample;
import com.mall.mansger.service.SmsCouponHistoryService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * 优惠券领取记录管理Service实现类
 * Created by macro on 2018/11/6.
 */
@Service
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {
    @Autowired
    private SmsCouponHistoryMapper historyMapper;
    @Override
    public List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        SmsCouponHistoryExample.Criteria criteria = example.createCriteria();
        if(couponId!=null){
            criteria.andCouponIdEqualTo(couponId);
        }
        if(useStatus!=null){
            criteria.andUseStatusEqualTo(useStatus);
        }
        if(!StrUtil.isEmpty(orderSn)){
            criteria.andOrderSnEqualTo(orderSn);
        }
        return historyMapper.selectByExample(example);
    }
}
