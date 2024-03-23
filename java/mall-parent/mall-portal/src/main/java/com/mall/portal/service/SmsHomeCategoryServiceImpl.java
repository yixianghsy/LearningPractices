package com.mall.portal.service;

import com.mall.marketing.dto.HomeGoodsSaleDTO;
import com.mall.portal.mapper.SmsHomeCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
