package net.xdclass.demo.intecpter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CustomWebMvcConfigurer  {
    
	//增加拦截器
    //public void addInterceptors(InterceptorRegistry registry){
      //  registry.addInterceptor(new LoginIntercepter())    //指定拦截器类
        //        .addPathPatterns("/Handles");        //指定该类拦截的url
    //}
	


}
