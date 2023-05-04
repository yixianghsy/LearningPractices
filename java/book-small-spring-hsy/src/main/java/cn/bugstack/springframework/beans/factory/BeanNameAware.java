package cn.bugstack.springframework.beans.factory;
/**
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 * @description Interface to be implemented by beans that want to be aware of their
 * bean name in a bean factory. Note that it is not usually recommended
 * that an object depend on its bean name, as this represents a potentially
 * brittle dependence on external configuration, as well as a possibly
 * unnecessary dependence on a Spring API.
 * 实现此接口，可以感知到所属得Bean对象名称
 */
public interface BeanNameAware {
    void setBeanName(String name);
}