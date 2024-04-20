package com.mall;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubboConfig
@DubboComponentScan("com.mall.marketing.service")
@SpringBootApplication
public class MarketingServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(MarketingServiceApp.class, args);
    }

}
