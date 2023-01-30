package com.xdclass.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.xdclass.search.dao"})
public class MyBatisConfig {
}
