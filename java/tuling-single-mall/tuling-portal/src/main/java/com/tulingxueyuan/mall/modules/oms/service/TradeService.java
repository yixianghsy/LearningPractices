package com.tulingxueyuan.mall.modules.oms.service;

import com.tulingxueyuan.mall.common.api.CommonResult;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
public interface TradeService {
    /**
     * 生成当面付二维码
     * @param orderId
     * @param payType  1.支付宝2.微信
     * @return
     */
    CommonResult tradeQrCode(Long orderId, Integer payType);
}
