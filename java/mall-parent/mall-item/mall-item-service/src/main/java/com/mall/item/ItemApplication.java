package com.mall.item;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@MapperScan(basePackages = {"com.mall.item.mapper"})
@SpringBootApplication
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }

}
