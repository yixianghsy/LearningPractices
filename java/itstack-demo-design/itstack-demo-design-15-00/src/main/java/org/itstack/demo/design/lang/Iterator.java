package org.itstack.demo.design.lang;

/**
 * 迭代器定义
 * @param <E>
 */
public interface Iterator<E> {
    //判断是否有下⼀个元素
    boolean hasNext();
    //获取下⼀个元素
    E next();
    
}
