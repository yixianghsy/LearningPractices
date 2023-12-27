package com.mall.portal.service;

import com.mall.order.model.OmsOrder;
import com.mall.portal.dto.ConfirmOrderDTO;
import com.mall.portal.dto.OrderDetailDTO;
import com.mall.portal.dto.OrderListDTO;
import com.mall.portal.dto.OrderParamDTO;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-21
 */
public interface OmsOrderService {

    /**
     * 初始化确认订单的商品和收货地址信息
     * @param ids
     * @return
     */
    ConfirmOrderDTO generateConfirmOrder(List<Long> ids);

    /**
     * 下单
     * @param paramDTO
     * @return
     */
    OmsOrder generateOrder(OrderParamDTO paramDTO);

    /**
     * 读取下单成功订单详情
     * @param id
     * @return
     */
    OrderDetailDTO getOrderDetail(Long id);

    void cancelOverTimeOrder();
    // TODO  需要自己写一个分页插件
//    IPage<OrderListDTO> getMyOrders(Integer pageSize, Integer pageNum);

    /**
     * 支付成功回调
     * @param orderId
     * @param payType
     */
    void paySuccess(Long orderId, Integer payType);
}
