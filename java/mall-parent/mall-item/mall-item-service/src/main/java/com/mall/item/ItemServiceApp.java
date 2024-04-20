package com.mall.item;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubboConfig
@DubboComponentScan("com.mall.item.service")
@SpringBootApplication
public class ItemServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApp.class, args);
    }

}
