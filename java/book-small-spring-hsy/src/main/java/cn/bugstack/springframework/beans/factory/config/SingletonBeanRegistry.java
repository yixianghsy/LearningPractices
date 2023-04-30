package cn.bugstack.springframework.beans.factory.config;

/**
 * 单例Bean注册表
 */
public interface SingletonBeanRegistry {
    /**
     * 返回在给定名称下注册得（原始)单例对象
     * @param beanName  要查找得bean得名称
     * @return 返回注册得单例对象
     */
    Object getSingleton(String beanName);

    /**
     * 注册单例对象
     * @param beanName Bean 对象名称
     * @param singletonObject Bean 对象
     */
    void registerSingleton(String beanName,Object singletonObject);
}
