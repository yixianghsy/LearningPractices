package com.e3mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.e3mall.search.mapper")
@SpringBootApplication
public class E3SearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3SearchServiceApplication.class, args);
	}

}
