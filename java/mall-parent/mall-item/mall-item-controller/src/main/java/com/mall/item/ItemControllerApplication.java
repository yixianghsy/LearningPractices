package com.mall.item;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@SpringBootApplication
public class ItemControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemControllerApplication.class, args);
    }

}