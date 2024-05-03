package com.mall;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubboConfig
@DubboComponentScan("com.mall.sso.service")
/*
scanBasePackages 扫面其他包路径，后期需要调整包
 */
@SpringBootApplication
public class SsoServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(SsoServiceApp.class, args);
    }

}
