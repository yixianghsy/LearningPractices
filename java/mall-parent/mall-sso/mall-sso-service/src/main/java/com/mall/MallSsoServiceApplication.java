package com.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.mall.mapper")
@SpringBootApplication
public class MallSsoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallSsoServiceApplication.class, args);
    }

}
