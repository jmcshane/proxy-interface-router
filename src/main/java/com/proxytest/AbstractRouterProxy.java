package com.proxytest;

import org.springframework.aop.framework.ProxyFactory;

import javax.naming.NamingException;

public abstract class AbstractRouterProxy implements RouterProxy {

    private ClassLoader classLoader;
    private Object proxy;
    private Class<?> businessInterface;

    @Override
    public Object getObject() throws Exception {
        return this.proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return this.businessInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setBusinessInterface(Class<?> businessInterface) {
        this.businessInterface = businessInterface;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void afterPropertiesSet() throws NamingException {
        if (this.businessInterface == null) {
            throw new IllegalArgumentException("businessInterface is required");
        }
        this.proxy = new ProxyFactory(this.businessInterface, this).getProxy(this.classLoader);
    }
}
