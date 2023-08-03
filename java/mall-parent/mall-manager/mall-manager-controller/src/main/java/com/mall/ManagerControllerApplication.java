package com.mall;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@SpringBootApplication
public class ManagerControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerControllerApplication.class, args);
    }

}
