package com.mall.portal.service;


import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.exception.BusinessException;
import com.mall.portal.domain.OmsOrderDetail;
import com.mall.portal.domain.ConfirmOrderResult;
import com.mall.portal.domain.OrderParam;
import com.mall.portal.dto.ConfirmOrderDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 前台订单管理Service
 * Created on 2018/8/30.
 */
public interface    OmsPortalOrderService {
    /**
     * 根据用户购物车信息生成确认单信息
     */
//    ConfirmOrderResult generateConfirmOrder(List<Long> itemIds) throws BusinessException;
    /**
     * 初始化确认订单的商品和收货地址信息
     * @param ids
     * @return
     */
    ConfirmOrderDTO generateConfirmOrder(List<Long> ids);
    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult generateOrder(OrderParam orderParam) throws BusinessException;

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    CommonResult getDetailOrder(Long orderId);

    /**
     * 支付成功后的回调
     */
    @Transactional
    Integer paySuccess(Long orderId,Integer payType);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     */
    @Transactional
    void cancelOrder(Long orderId);

    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     *      受影响的行
     */
    @Transactional
    int deleteOrder(Long orderId);
    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(Long orderId);

    /**
     * 查询会员的订单
     * @param pageSize
     * @param pageNum
     * @param memberId
     *      会员ID
     * @param status
     *      订单状态
     * @return
     */
    CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status);
    /**
     * 确认收货
     */
    void confirmReceiveOrder(Long orderId);

    /**
     * 分页获取用户订单
     */
    CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize);
    /**
     * 根据订单ID获取订单详情
     */
    OmsOrderDetail detail(Long orderId);

    /**
     * 根据orderSn来实现的支付成功逻辑
     */

    void paySuccessByOrderSn(String orderSn, Integer payType);
}
