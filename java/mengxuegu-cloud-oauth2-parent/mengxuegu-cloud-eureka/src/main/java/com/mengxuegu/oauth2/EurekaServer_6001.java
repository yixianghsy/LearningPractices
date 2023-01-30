package com.mengxuegu.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@EnableEurekaServer // 标识它是Eureka服务器
@SpringBootApplication
public class EurekaServer_6001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer_6001.class, args);
    }

}
