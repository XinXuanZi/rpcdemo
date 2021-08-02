package com.example.rpc1.client;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

/**
 * @author XuanZi
 * @date 2021/8/222:30
 */
public class RpcFactoryBeanRegister implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        Set<Class<?>> classes = loadClass();
        for (Class<?> aClass : classes) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
            GenericBeanDefinition rawBeanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();


            
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Set<Class<?>> loadClass() {
        Reflections reflections = new Reflections("com.example.rpc1.client");
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(RpcServer.class);

        return classesList;
    }
}
