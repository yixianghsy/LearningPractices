package org.itstack.demo.design;

/**
 * 懒汉模式(线程不安全)
 * 以上这种⽅式在我们平常的业务开发中⾮常场常⻅，这样静态类的⽅式可以在第⼀次运⾏的时候直
 * 接初始化Map类，同时这⾥我们也不需要到延迟加载在使⽤。
 * 在不需要维持任何状态下，仅仅⽤于全局访问，这个使⽤使⽤静态类的⽅式更加⽅便。
 * 但如果需要被继承以及需要维持⼀些特定状态的情况下，就适合使⽤单例模式。
 */
public class Singleton_01 {

    private static Singleton_01 instance;

    private Singleton_01() {
    }

    public static Singleton_01 getInstance(){
        if (null != instance) return instance;
        return new Singleton_01();
    }

}
