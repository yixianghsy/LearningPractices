package com.e3mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.e3mall.mapper")
@SpringBootApplication
public class  E3SsoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3SsoServiceApplication.class, args);
	}

}
