package com.tulingxueyuan.mall.modules.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tulingxueyuan.mall.dto.HomeGoodsSaleDTO;
import com.tulingxueyuan.mall.modules.sms.mapper.SmsHomeCategoryMapper;
import com.tulingxueyuan.mall.modules.sms.model.SmsHomeCategory;
import com.tulingxueyuan.mall.modules.sms.service.SmsHomeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-16
 */
@Service
public class SmsHomeCategoryServiceImpl extends ServiceImpl<SmsHomeCategoryMapper, SmsHomeCategory> implements SmsHomeCategoryService {

    @Autowired
    SmsHomeCategoryMapper homeCategoryMapper;

    @Override
    public List<HomeGoodsSaleDTO> getGoodsSale() {
        return homeCategoryMapper.getHomeCategoryWithProduct();
    }
}
