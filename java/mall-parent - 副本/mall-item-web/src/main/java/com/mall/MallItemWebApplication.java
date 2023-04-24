package com.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class MallItemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallItemWebApplication.class, args);
    }
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		return new ServletRegistrationBean(dispatcherServlet,"*.html");
	}

}
