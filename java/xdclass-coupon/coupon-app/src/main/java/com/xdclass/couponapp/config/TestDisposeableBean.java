package com.xdclass.couponapp.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class TestDisposeableBean implements DisposableBean {



    @Override
    public void destroy() throws Exception {
        System.out.println("测试TestDisposeableBean已经销毁");
    }
}
