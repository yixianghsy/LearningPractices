package com.mall.order.service;


import com.mall.utils.E3Result;
import com.mall.vo.OrderInfo;

public interface OrderService {
    E3Result createOrder(OrderInfo orderInfo);
    void  testOrderId(String orderId);
}
