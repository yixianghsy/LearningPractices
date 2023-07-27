package com.mall.order.controller;


import com.mall.order.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * 文件描述
 **/
@Controller
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Resource
    private OrderService orderService;

    @GetMapping("/test")
    @ResponseBody
    public String  getItemList(){

        orderService.testOrderId("232");


        return "没有报错";
    }
}
