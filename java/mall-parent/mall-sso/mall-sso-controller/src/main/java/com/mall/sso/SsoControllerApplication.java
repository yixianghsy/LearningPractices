package com.mall.sso;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
//@ServletComponentScan
@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@SpringBootApplication
public class SsoControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoControllerApplication.class, args);
    }
    //这个bean 或导致无法访问
//    @Bean
//    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
//        return new ServletRegistrationBean(dispatcherServlet,"*.html");
//    }
}
