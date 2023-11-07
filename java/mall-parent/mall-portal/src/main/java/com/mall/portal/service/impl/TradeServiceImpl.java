package com.mall.portal.service.impl;

import com.mall.api.CommonResult;
import com.mall.portal.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *                  ,;,,;
 *                ,;;'(    社
 *      __      ,;;' ' \   会
 *   /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 *,;' \    /-.,,(   ) \    码
 *     ) /       ) / )|    农
 *     ||        ||  \)
 *     (_\       (_\
 * @author ：杨过
 * @date ：Created in 2020/1/12 19:37
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description: 交易服务
 **/
//TODO  支付宝交易
@Slf4j
@Service
public class TradeServiceImpl implements TradeService {
    @Override
    public CommonResult tradeQrCode(Long orderId, Integer payType) {
        return null;
    }

    @Override
    public CommonResult tradeStatusQuery(Long orderId, Integer payType) {
        return null;
    }
}
