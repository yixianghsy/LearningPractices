package org.itstack.demo.design.test;

import org.itstack.demo.design.LoginSsoDecorator;
import org.itstack.demo.design.SsoInterceptor;
import org.junit.Test;

/**
 * 装饰器的核⼼就是再不改原有类的基础上 )
 * 给类新增功能。不改变原有类，可能有的⼩伙伴会想到继承、AOP切⾯，当然这些⽅式都可以实现，但
 * 是使⽤装饰器模式会是另外⼀种思路更为灵活，可以避免继承导致的⼦类过多，也可以避免AOP带来的
 * 复杂性
 */
public class ApiTest {

    @Test
    public void test_LoginSsoDecorator() {
        LoginSsoDecorator ssoDecorator = new LoginSsoDecorator(new SsoInterceptor());
        String request = "1successhuahua";
        boolean success = ssoDecorator.preHandle(request, "ewcdqwt40liuiu", "t");
        System.out.println("登录校验：" + request + (success ? " 放行" : " 拦截"));
    }

}
