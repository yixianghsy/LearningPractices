package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.UserService;
import org.junit.Test;

/**
 *
 *
 *
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 * @description 单元测试
 * @date 2022/03/10
 *
 *
 */
public class ApiTest {

    @Test
    public void test_xml() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
        //ApplicationContextAware：cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext@19bb089b加载类得地方
        System.out.println("ApplicationContextAware："+userService.getApplicationContext());
        //BeanFactoryAware：cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory@4563e9ab这个是bean存放的地方
        System.out.println("BeanFactoryAware："+userService.getBeanFactory());
    }

}
