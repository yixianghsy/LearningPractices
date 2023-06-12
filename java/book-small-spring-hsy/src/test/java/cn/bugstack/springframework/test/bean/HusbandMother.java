package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 *
 *
 * 婆婆类，代理了媳妇的妈妈的职责的类
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 * @description 代理类
 * @date 2022/3/16
 *
 *
 */
public class HusbandMother implements FactoryBean<IMother> {

    @Override
    public IMother getObject() throws Exception {
        return (IMother) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IMother.class}, (proxy, method, args) -> "婚后媳妇妈妈的职责被婆婆代理了！" + method.getName());
    }

    @Override
    public Class<?> getObjectType() {
        return IMother.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}