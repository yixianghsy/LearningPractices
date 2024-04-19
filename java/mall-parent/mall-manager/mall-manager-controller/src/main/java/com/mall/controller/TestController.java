package com.mall.controller;

import com.mall.marketing.service.SmsCouponHistoryService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class TestController {


    @Reference
    SmsCouponHistoryService smsCouponHistoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public  String  testHistory()
    {
        smsCouponHistoryService.list(null,null,null,5,1);
        return  "查询成功";
    }
}
