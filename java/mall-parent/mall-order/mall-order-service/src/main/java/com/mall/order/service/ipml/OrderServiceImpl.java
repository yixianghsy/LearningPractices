package com.mall.order.service.ipml;
import com.mall.order.mapper.TbOrderMapper;
import com.mall.order.service.OrderService;
import com.mall.utils.E3Result;
import com.mall.vo.OrderInfo;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;


/**
 * 订单处理服务
 * <p>Title: OrderServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private TbOrderMapper tbOrderMapper;

    @Override
    public E3Result createOrder(OrderInfo orderInfo) {

        //返回E3Result，包含订单号

        return E3Result.ok("325");
    }
    @Override
    public   void   testOrderId(String orderId) {
        System.out.printf("...testOrderId开始执行...");

        tbOrderMapper.selectByPrimaryKey(orderId);

        System.out.printf("...testOrderId执行完毕...");
    }
}
