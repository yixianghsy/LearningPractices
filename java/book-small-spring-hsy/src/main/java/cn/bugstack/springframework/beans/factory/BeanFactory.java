package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;

/**
 * 定义bean工厂接口
 */
public interface BeanFactory {
    /**
     * 返回bean的实例对象
     * @param name 要检索bean得名称
     * @return 实例化得bean对象
     * @throws BeansException 不能直接获取Bean，则抛出异常
     */
    Object getBean(String name) throws BeansException;

    /**
     * 返回含构造函数Bean实例对象
     * @param name 要检索得Bean得名称
     * @param args 构造函数入参
     * @return 实例化Bean对象
     * @throws BeansException 不能获取Bean 对象 ，则抛出异常
     */

    Object getBean(String name ,Object... args) throws BeansException;

    /**
     * 返回指定泛型得对象
     * @param name 要检索得Bean得名称
     * @param requiredType 类型
     * @return
     * @param <T> 泛型
     * @throws BeansException 不能获取Bean对象则抛出异常
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
