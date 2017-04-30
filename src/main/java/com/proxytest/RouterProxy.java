package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public interface RouterProxy extends InitializingBean, FactoryBean<Object>,
    BeanClassLoaderAware, MethodInterceptor {

    void modifyWeight(String key, int weight);

    void newRoute(String key, int weight);

}
