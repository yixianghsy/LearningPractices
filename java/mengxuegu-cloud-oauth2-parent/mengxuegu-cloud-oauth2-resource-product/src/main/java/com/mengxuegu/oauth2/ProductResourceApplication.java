package com.mengxuegu.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 资源服务器-创建商品资源模块
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@EnableEurekaClient
@SpringBootApplication
public class ProductResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductResourceApplication.class, args);
    }
}
