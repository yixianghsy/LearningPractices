package com.mall.marketing.service.impl;

import com.mall.marketing.dto.HomeGoodsSaleDTO;

import com.mall.marketing.mapper.SmsHomeCategoryMapper;
import com.mall.marketing.service.SmsHomeCategoryService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class SmsHomeCategoryServiceImpl implements SmsHomeCategoryService {
    @Autowired
    SmsHomeCategoryMapper homeCategoryMapper;
    @Override
    public List<HomeGoodsSaleDTO> getGoodsSale() {
        return homeCategoryMapper.getHomeCategoryWithProduct();
    }
}
