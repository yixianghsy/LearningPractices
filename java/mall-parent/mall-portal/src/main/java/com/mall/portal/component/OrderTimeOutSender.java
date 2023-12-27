package com.mall.portal.component;

import com.mall.portal.service.OmsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 *
 * 订单超时取消并解锁库存的定时器
 */
@Component
@Slf4j
public class OrderTimeOutSender {

    @Autowired
    private OmsOrderService orderService;

    // 秒 分 小时 日期 月份 星期  年（可选）
    //    / 代表隔多少时间允许
    //     * 任何时间
    //    ?  代表无任何指定  日期、星期才能指定
    // 1分钟执行一次
    //@Scheduled(cron="0 0/50 * ? * ?")
    private  void cancelOverTimeOrder(){
        log.info("--------OrderTimeOutSender订单超时取消并解锁库存的定时器开始----------");
        orderService.cancelOverTimeOrder();
        log.info("--------OrderTimeOutSender订单超时取消并解锁库存的定时器结束----------");
    }
}
