package com.mall.order.service;

import com.mall.order.pojo.OrderInfo;
import com.mall.utils.E3Result;

public interface OrderService {
    E3Result createOrder(OrderInfo orderInfo);
}
