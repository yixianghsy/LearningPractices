package net.xdclass.base_project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //一个注解顶下面3个
@MapperScan("net.xdclass.base_project.mapper")
public class XdclassApplication  {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(XdclassApplication.class, args);
    }
    

}