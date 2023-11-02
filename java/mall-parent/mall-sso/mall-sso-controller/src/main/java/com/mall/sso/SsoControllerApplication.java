package com.mall.sso;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
/*
scanBasePackages 扫面其他包路径，后期需要调整包
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},scanBasePackages = { "com.mall.*"})
public class SsoControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoControllerApplication.class, args);
    }
}
