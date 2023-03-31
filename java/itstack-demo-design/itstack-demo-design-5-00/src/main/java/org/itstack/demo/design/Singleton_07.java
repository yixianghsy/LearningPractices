package org.itstack.demo.design;

/**
 * Effective Java作者推荐的枚举单例(线程安全)
 *Effective Java 作者推荐使⽤枚举的⽅式解决单例模式，此种⽅式可能是平时最少⽤到的。
 * 这种⽅式解决了最主要的；线程安全、⾃由串⾏化、单⼀实例。
 */
public enum Singleton_07 {

    INSTANCE;
    public void test(){
        System.out.println("hi~");
    }

}
