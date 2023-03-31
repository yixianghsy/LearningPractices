package org.itstack.demo.design.test;

import org.itstack.demo.design.Singleton_00;
import org.itstack.demo.design.Singleton_07;
import org.junit.Test;

/**
 * 单例模式设计学习
 * 单例模式可以说是整个设计中最简单的模式之⼀，⽽且这种⽅式即使在没有看设计模式相关资料也会常
 * ⽤在编码开发中。
 * 因为在编程开发中经常会遇到这样⼀种场景，那就是需要保证⼀个类只有⼀个实例哪怕多线程同时访
 * 问，并需要提供⼀个全局访问此实例的点。
 * 综上以及我们平常的开发中，可以总结⼀条经验，单例模式主要解决的是，⼀个全局使⽤的类频繁的创
 * 建和消费，从⽽提升提升整体的代码的性能。
 */
public class ApiTest {

    @Test
    public void test() {
        Singleton_07.INSTANCE.test();
    }

}
