package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;

/**
 *
 * @description Defines a factory which can return an Object instance (possibly shared or independent) when invoked.
 * @date 2022/3/16
 *  /CodeDesignTutorials
 *
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
