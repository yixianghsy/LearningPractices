package com.e3mall.order.service;


import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.utils.E3Result;

public interface OrderService {

	E3Result createOrder(OrderInfo orderInfo);
}
