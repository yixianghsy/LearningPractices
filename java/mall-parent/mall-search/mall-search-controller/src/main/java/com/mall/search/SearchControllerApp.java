package com.mall.search;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@SpringBootApplication
public class SearchControllerApp {

    public static void main(String[] args) {
        SpringApplication.run(SearchControllerApp.class, args);
    }
}
