package com.e3mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@ServletComponentScan
public class  E3SearchWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3SearchWebApplication.class, args);
	}
//
//	@Bean
//	public ServletRegistrationBean servletRegistrationBean() {
//		return new ServletRegistrationBean(new DispatcherServlet(), "*.html");
//	}
@Bean
public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
	return new ServletRegistrationBean(dispatcherServlet,"*.html");
}


}
