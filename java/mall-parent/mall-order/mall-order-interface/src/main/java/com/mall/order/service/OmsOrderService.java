package com.mall.order.service;

import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.order.dto.*;
import com.mall.order.model.OmsOrder;
import com.mall.order.model.OmsOrderItem;

import java.util.List;

/**
 * 订单管理Service
 * Created by macro on 2018/10/11.
 */
public interface OmsOrderService {

    

    /**
     * 订单查询
     */
    CommonPage list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量发货
     */

    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 批量关闭订单
     */

    int close(List<Long> ids, String note);

    /**
     * 批量删除订单
     */
    int delete(List<Long> ids);

    /**
     * 获取指定订单详情
     */
    OmsOrderDetail detail(Long id);

    /**
     * 修改订单收货人信息
     */

    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

    /**
     * 修改订单费用信息
     */

    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

    /**
     * 修改订单备注
     */

    int updateNote(Long id, String note, Integer status);
    /**
     * 支付成功回调
     * @param orderId
     * @param payType
     */
    void paySuccess(Long orderId, Integer payType);

    void orderInsert(OmsOrder omsOrder);

    void insertList(List<OmsOrderItem> list);

    OmsOrderDetail getDetail(Long orderId);

    CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status);

    void cancelOverTimeOrder();

    OrderDetailDTO getOrderDetail(Long orderId);
}
