package com.mall.portal.service;

import com.mall.mansger.mapper.SmsHomeAdvertiseMapper;
import com.mall.mansger.model.SmsHomeAdvertiseExample;
import com.mall.mansger.model.SmsHomeAdvertise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Override
    public List<SmsHomeAdvertise> getHomeBanners() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andTypeEqualTo(1).andStatusEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }
}
