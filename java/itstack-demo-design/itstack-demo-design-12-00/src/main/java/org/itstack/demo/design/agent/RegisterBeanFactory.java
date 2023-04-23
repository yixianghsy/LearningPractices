package org.itstack.demo.design.agent;

import org.itstack.demo.design.IUserDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
//将Bean定义注册到Spring容器
public class RegisterBeanFactory implements BeanDefinitionRegistryPostProcessor {
    
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //GenericBeanDefinition ，
        //⽤于定义⼀个bean的基本信息setBeanClass(MapperFactoryBean.class); ，
        //也包括可以透传给构造函数信息 addGenericArgumentValue(IUserDao.class);
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MapperFactoryBean.class);
        beanDefinition.setScope("singleton");
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(IUserDao.class);

        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, "userDao");
        //bean的注册,也就是注册到 DefaultListableBeanFactory 中
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // left intentionally blank
    }

}
