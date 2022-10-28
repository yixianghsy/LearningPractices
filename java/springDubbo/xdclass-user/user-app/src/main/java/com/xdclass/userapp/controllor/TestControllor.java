package com.xdclass.userapp.controllor;

import com.xdclass.userapp.service.CouponService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestControllor {


    @Resource
    private CouponService couponService;


    @RequestMapping("/test")
    public String test(){
        return "111";
    }

    @RequestMapping("/test1")
    public String testQuery(String id){
         if(id==null){
             return "";
         }
         return couponService.query();
    }

}
