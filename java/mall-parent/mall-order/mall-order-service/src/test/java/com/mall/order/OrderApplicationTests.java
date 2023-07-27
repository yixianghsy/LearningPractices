package com.mall.order;

import com.mall.order.mapper.TbOrderMapper;
import com.mall.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class OrderApplicationTests {
    @Resource
    private OrderService orderService;
    @Test
    void contextLoads() {
        orderService.testOrderId("235");
    }

}
