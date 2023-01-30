package com.xdclass.couponapp;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubboConfig
@DubboComponentScan({"com.xdclass.userapp.service.dubbo","com.xdclass.couponapp.service"})
@SpringBootApplication
@EnableScheduling
@MapperScan("com.xdclass.couponapp.mapper")
public class CouponAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponAppApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行JVM ShutdownHook！！");
            }
        }));
    }

}
