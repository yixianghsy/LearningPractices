package com.mall.search;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SearchControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchControllerApplication.class, args);
    }
}
