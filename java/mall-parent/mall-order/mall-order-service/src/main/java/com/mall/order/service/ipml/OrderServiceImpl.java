package com.mall.order.service.ipml;

import com.mall.modules.order.TbOrder;
import com.mall.order.mapper.TbOrderMapper;
import com.mall.order.service.OrderService;
import com.mall.utils.E3Result;
import com.mall.vo.OrderInfo;
import org.apache.dubbo.config.annotation.Service;


import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private TbOrderMapper orderMapper;
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        return null;
    }

    @Override
    public String testOrderId(String orderId) {
        System.out.println("执行开始");
        TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderId);
        System.out.println("执行结束"+tbOrder.getOrderId());
        return "ok";
    }
}
