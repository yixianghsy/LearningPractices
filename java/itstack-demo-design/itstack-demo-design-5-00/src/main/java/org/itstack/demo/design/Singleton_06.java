package org.itstack.demo.design;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS「AtomicReference」(线程安全)
 * java并发库提供了很多原⼦类来⽀持并发访问的数据安全
 * 性； AtomicInteger 、 AtomicBoolean 、 AtomicLong 、 AtomicReference 。
 * AtomicReference 可以封装引⽤⼀个V实例，⽀持并发访问如上的单例⽅式就是使⽤了这样的⼀个
 * 特点。
 * 使⽤CAS的好处就是不需要使⽤传统的加锁⽅式保证线程安全，⽽是依赖于CAS的忙等算法，依赖
 * 于底层硬件的实现，来保证线程安全。相对于其他锁的实现没有线程的切换和阻塞也就没有了额外
 * 的开销，并且可以⽀持较⼤的并发性。
 * 当然CAS也有⼀个缺点就是忙等，如果⼀直没有获取到将会处于死循环中。
 */
public class Singleton_06 {

    private static final AtomicReference<Singleton_06> INSTANCE = new AtomicReference<Singleton_06>();

    private static Singleton_06 instance;

    private Singleton_06() {
    }

    public static final Singleton_06 getInstance() {
        for (; ; ) {
            Singleton_06 instance = INSTANCE.get();
            if (null != instance) return instance;
            INSTANCE.compareAndSet(null, new Singleton_06());
            return INSTANCE.get();
        }
    }

    public static void main(String[] args) {
        System.out.println(Singleton_06.getInstance()); // org.itstack.demo.design.Singleton_06@2b193f2d
        System.out.println(Singleton_06.getInstance()); // org.itstack.demo.design.Singleton_06@2b193f2d
    }


}
