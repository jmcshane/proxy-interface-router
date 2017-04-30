package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ProxyRouterBean implements RouterProxy, InitializingBean, FactoryBean<Object>, BeanClassLoaderAware, MethodInterceptor {

    private ClassLoader classLoader;
    private Object proxy;
    private Class<?> businessInterface;

    private Map<String, MethodInterceptor> interceptors = new HashMap<>();

    private List<String> randomInterceptorSelector = new ArrayList<>();
    private Integer index = 0;

    public void modifyWeight(String key, int weight) {
        while (randomInterceptorSelector.contains(key)) {
            randomInterceptorSelector.remove(key);
        }
        IntStream.range(0, weight).forEach(i -> randomInterceptorSelector.add(key));
    }

    public void newRoute(Map.Entry<String, MethodInterceptor> interceptor, int weight) {
        String key = interceptor.getKey();
        interceptors.put(key, interceptor.getValue());
        while (randomInterceptorSelector.contains(key)) {
            randomInterceptorSelector.remove(key);
        }
        IntStream.range(0, weight).forEach(i -> randomInterceptorSelector.add(key));
    }

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

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (randomInterceptorSelector.isEmpty()) {
            return null;
        }
        if (index >= randomInterceptorSelector.size()) {
            index = 0;
        }
        String key = randomInterceptorSelector.get(index);
        index++;
        return interceptors.get(key).invoke(invocation);
    }
}
