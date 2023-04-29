package com.mall.order.service.ipml;

import com.mall.mapper.TbOrderItemMapper;
import com.mall.mapper.TbOrderMapper;
import com.mall.mapper.TbOrderShippingMapper;
import com.mall.mapper.pojo.TbOrderItem;
import com.mall.mapper.pojo.TbOrderShipping;
import com.mall.order.pojo.OrderInfo;
import com.mall.order.service.OrderService;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;


import java.util.Date;
import java.util.List;

/**
 * 订单处理服务
 * <p>Title: OrderServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_START}")
    private String ORDER_ID_START;
    @Value("${ORDER_DETAIL_ID_GEN_KEY}")
    private String ORDER_DETAIL_ID_GEN_KEY;
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //生成订单号。使用redis的incr生成。
        if (!stringRedisTemplate.hasKey(ORDER_ID_GEN_KEY)){
            stringRedisTemplate.opsForValue().set(ORDER_ID_GEN_KEY,ORDER_ID_START);
        }
        String orderId =  stringRedisTemplate.boundValueOps(ORDER_ID_GEN_KEY).increment(1).toString();
        //补全orderInfo的属性
        orderInfo.setOrderId(orderId);
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表
        orderMapper.insert(orderInfo);
        //向订单明细表插入数据。
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem:orderItems){
            //生成明细id
            String odId =  stringRedisTemplate.boundValueOps(ORDER_DETAIL_ID_GEN_KEY).increment(1).toString();
            //补全pojo的属性
            tbOrderItem.setId(odId);
            tbOrderItem.setOrderId(orderId);
            //向明细表插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        //返回E3Result，包含订单号

        return E3Result.ok(orderId);
    }
}
