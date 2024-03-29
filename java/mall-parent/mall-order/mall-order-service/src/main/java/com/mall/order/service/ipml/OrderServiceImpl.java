package com.mall.order.service.ipml;

import com.mall.modules.order.TbOrder;
import com.mall.modules.order.TbOrderItem;
import com.mall.modules.order.TbOrderShipping;
import com.mall.order.mapper.TbOrderItemMapper;
import com.mall.order.mapper.TbOrderMapper;
import com.mall.order.mapper.TbOrderShippingMapper;
import com.mall.order.service.OrderService;
import com.mall.utils.E3Result;
import com.mall.vo.OrderInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_ID_BEGIN}")
    private Integer ORDER_ID_BEGIN;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        // 1、接收表单的数据
        // 2、生成订单id
        if (!redisTemplate.hasKey(ORDER_GEN_KEY)) {
            //设置初始值
            redisTemplate.opsForValue().set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
        }
        String orderId = redisTemplate.opsForValue().increment(ORDER_GEN_KEY, 1L).toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        // 3、向订单表插入数据。
        orderMapper.insert(orderInfo);
        // 4、向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id
            Long orderItemId = redisTemplate.opsForValue().increment(ORDER_ITEM_ID_GEN_KEY, 1L);
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId);
            //插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        // 5、向订单物流表插入数据。
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        // 6、返回e3Result。
        return E3Result.ok(orderId);
    }


}
