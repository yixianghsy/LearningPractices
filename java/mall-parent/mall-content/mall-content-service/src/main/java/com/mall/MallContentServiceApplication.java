package com.mall;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.mall.mapper")
@SpringBootApplication
public class MallContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallContentServiceApplication.class, args);
    }

}
