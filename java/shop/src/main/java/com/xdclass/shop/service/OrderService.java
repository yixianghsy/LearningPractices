/**
 *
 */
package com.xdclass.shop.service;

import com.alibaba.fastjson.JSON;
import com.xdclass.shop.common.Constants;
import com.xdclass.shop.common.OrderCouponDto;
import com.xdclass.shop.common.Page;
import com.xdclass.shop.controller.TestController;
import com.xdclass.shop.model.*;
import com.xdclass.shop.repository.CouponRepository;
import com.xdclass.shop.repository.OrderItemRepository;
import com.xdclass.shop.repository.OrderRepository;
import com.xdclass.shop.repository.UserCouponRepository;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Daniel
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderDao;
    @Autowired
    OrderItemRepository orderItemDao;
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    UserCouponRepository userCouponRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private DefaultMQProducer producer;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);


    /**
     * 新建订单
     *
     * @param order
     * @param orderItemList
     * @param userAddress
     */
    public String addOrder(Order order, List<OrderItem> orderItemList, UserAddress userAddress, String userCouponCode) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserCouponCode(userCouponCode);
        if (CollectionUtils.isEmpty(userCoupons)) {
            return "下单失败，优惠券不可用";
        }
        UserCoupon userCoupon = userCoupons.get(0);
        Coupon conpon = couponRepository.findOne(userCoupon.getCouponId());
        if (conpon == null) {
            return "下单失败，优惠券不可用";
        }
        int reduceAmount = conpon.getReduceAmount();
        int achieveAmount = conpon.getAchieveAmount();
        if (achieveAmount > order.getFinalPrice()) {
            return "满减券未达到满减要求不可用";
        }
        order.setFinalPrice(order.getFinalPrice() - reduceAmount);
        userAddressService.save(userAddress);
        save(order);
        for (OrderItem orderItem : orderItemList) {
            orderItemDao.save(orderItem);
        }
        userCoupon.setOrderId(order.getId());
        userCoupon.setStatus(1);
        userCouponRepository.saveAndFlush(userCoupon);
        updateCouponCode(order.getId(), order.getUser().getId(), userCouponCode);
        return null;
    }

    //用户下单跟新coupon和order关联关系
    private void updateCouponCode(int orderId, int userId, String couponCode) {
        OrderCouponDto dto = new OrderCouponDto(couponCode, orderId, userId);
        Message message = new Message("saveOrder", "Tag1", "12345", dto.toString().getBytes());
        // 这里用到了这个mq的异步处理，类似ajax，可以得到发送到mq的情况，并做相应的处理
        //不过要注意的是这个是异步的
        send(message);
    }

    public void save(Order order) {
        orderDao.save(order);
    }

    public Order findById(Integer id) {
        return orderDao.findOne(id);
    }

    public List<Order> findAll() {
        return orderDao.findAll();
    }

    public List<Order> findOrders(Page<Order> page) {
        page.setResult(orderDao.findAll(page.getPageable()).getContent());
        page.setTotalCount(orderDao.count());
        return page.getResult();
    }

    public List<Order> findOrders(Page<Order> page, Integer userId) {
        page.setResult(orderDao.findByUserId(userId, page.getPageable()).getContent());
        page.setTotalCount(orderDao.countByUserId(userId));
        return null;
    }

    /**
     * 删除订单以及订单相关信息
     *
     * @param id 订单ID
     */
    public void deleteOrder(Integer id) {
        orderItemDao.deleteByOrderId(id);
        orderDao.delete(id);
    }

    /**
     * 修改订单状态
     *
     * @param orderID
     * @param status
     */
    public void updateOrderStatus(Integer orderID, Integer status) {
        Order order = orderDao.findOne(orderID);
        order.setStatus(status);
        //状态修改时修改相应时间数据
        if (status == Constants.OrderStatus.PAYED) {
            order.setPayTime(new Date());
        } else if (status == Constants.OrderStatus.SHIPPED) {
            order.setShipTime(new Date());
        } else if (status == Constants.OrderStatus.ENDED) {
            order.setConfirmTime(new Date());
        }
        orderDao.save(order);
    }

    /**
     * 验证订单归属人
     *
     * @param orderId
     * @param userId
     * @return
     */
    public boolean checkOwned(Integer orderId, Integer userId) {
        return orderDao.findOne(orderId).getUser().getId().equals(userId);
    }


    private void payResutlt(int orderId, int userId) {
        OrderCouponDto dto = new OrderCouponDto(orderId, userId,0);
        Message message = new Message("payResult", "Tag1", "12345", dto.toString().getBytes());
        // 这里用到了这个mq的异步处理，类似ajax，可以得到发送到mq的情况，并做相应的处理
        //不过要注意的是这个是异步的
        send(message);
    }

    private void send(Message message) {
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("传输成功");
                    log.info(JSON.toJSONString(sendResult));
                }

                @Override
                public void onException(Throwable e) {
                    log.error("传输失败", e);
                }
            });
        } catch (Exception e) {
            log.error("传输失败", e);
        }
    }


    public void pay(Integer orderId) {
        Order order = orderDao.findOne(orderId);
        order.setStatus(Constants.OrderStatus.PAYED);
        order.setPayTime(new Date());
        orderDao.save(order);
        payResutlt(orderId,order.getUser().getId());
        log.info("send pay result success");
    }
}
