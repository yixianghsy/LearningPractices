package com.mall.sso;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
/**
 * 目前推测可能是分页依赖中的数据源《com.github.pagehelper》
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SsoControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoControllerApplication.class, args);
    }
}
