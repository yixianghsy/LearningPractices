package com.mall.cart;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@EnableDubboConfig
@DubboComponentScan("com.mall.*.service")
@SpringBootApplication
public class CartControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartControllerApplication.class, args);
	}

	/**
	 * 这个bean 我也不知道用在哪里。。
	 *
	 * @param dispatcherServlet
	 * @return
	 */
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		return new ServletRegistrationBean(dispatcherServlet,"*.html,*.action");
	}
}
