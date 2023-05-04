package cn.bugstack.springframework.context;

import cn.bugstack.springframework.beans.BeansException;

/**
 * @description SPI 接口配置应用上下文 SPI interface to be implemented by most if not all application contexts.
 * Provides facilities to configure an application context in addition
 * to the application context client methods in the
 */
public interface ConfigurableApplicationContext  extends ApplicationContext{
    /**
     * 刷新容器
     * @throws BeansException
     */
    void refresh() throws BeansException;

    /**
     * 注册虚拟键钩子得方法
     */
    void registerShutdownHook();

    /**
     * 关闭虚拟机钩子得方法
     */
    void close();
}
