package cn.bugstack.springframework.beans.factory;

/**
 * 实现此接口得Bean 对象。会在BeanFactory 设置属性后做出相应处理，，如执行自定义初始化，或者仅仅检查是否设置了所有强制属性
 * 通俗理解就是初始化接口
 *
 */
public interface InitializingBean {
    /**
     *  Bean 处理了属性填充调用
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
