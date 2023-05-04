package cn.bugstack.springframework.beans.factory;

/**
 * 实现此接口，可以感知到所属得ClassLoader加载器
 */
public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader classLoader);
}
