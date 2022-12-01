package com.tulingxueyuan.mall.modules.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tulingxueyuan.mall.dto.HomeGoodsSaleDTO;
import com.tulingxueyuan.mall.modules.sms.model.SmsHomeCategory;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-16
 */
public interface SmsHomeCategoryService extends IService<SmsHomeCategory> {

    List<HomeGoodsSaleDTO> getGoodsSale();
}
