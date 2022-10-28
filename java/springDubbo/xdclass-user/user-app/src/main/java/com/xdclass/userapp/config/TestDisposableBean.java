package com.xdclass.userapp.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class TestDisposableBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("测试 Bean 已销毁 ...");
    }
}