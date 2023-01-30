package com.mengxuegu.oauth2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@EnableZuulProxy //开启zuul的功能
@EnableEurekaClient
@SpringBootApplication
public class ZuulServer_7001 {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServer_7001.class, args);
    }

}